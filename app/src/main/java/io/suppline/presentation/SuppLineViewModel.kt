package io.suppline.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.suppline.domain.models.DailySupplementation
import io.suppline.domain.models.Supplement
import io.suppline.domain.useCase.GetDailySupplementationUseCase
import io.suppline.domain.useCase.SaveDailySupplementationUseCase
import io.suppline.domain.utils.extensions.localTimeToEpochMillis
import io.suppline.domain.utils.validators.TimeValidator
import io.suppline.presentation.events.AddEditSupplementEvent
import io.suppline.presentation.models.Notification
import io.suppline.presentation.states.NotificationState
import io.suppline.presentation.states.SuppLineState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class SuppLineViewModel @Inject constructor(
    private val getSupplementation: GetDailySupplementationUseCase,
    private val saveSupplementation: SaveDailySupplementationUseCase
) : ViewModel(), SuppLineViewModelContract {

    private var _state = mutableStateOf(SuppLineState())
    val state: State<SuppLineState> = _state

    private var _updateNotification: MutableSharedFlow<NotificationState?> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val updateNotification: SharedFlow<NotificationState?> = _updateNotification

    init {
        fetchData()
    }

    private fun fetchData() {
        //fetch from preferences or create new based on default
        val supplementation = getSupplementation()
        _state.value = state.value.copy(
            date = supplementation.date,
            supplements = supplementation.supplements
        )
        setProgress()
    }

    override fun onEvent(event: AddEditSupplementEvent) {
        when (event) {
            is AddEditSupplementEvent.OnNameChange -> {
                _state.value = state.value.copy(
                    editedItem = event.supplement.copy(name = event.name)
                )
            }

            is AddEditSupplementEvent.OnDescriptionChange -> {
                _state.value = state.value.copy(
                    editedItem = event.supplement.copy(description = event.description)
                )
            }

            is AddEditSupplementEvent.OnRemove -> {
                removeSupplement(event.supplement)
                _state.value = state.value.copy(
                    editedItem = null,
                    configItemIndex = null
                )
            }

            is AddEditSupplementEvent.Accept -> {
                val updatedItem = state.value.editedItem!!

                val index = state.value.supplements.indexOfFirst { it.id == updatedItem.id }
                if (index == -1) addSupplement(updatedItem)
                else updateSupplement(updatedItem)

                _state.value = state.value.copy(
                    editedItem = null
                )
            }

            is AddEditSupplementEvent.Reject -> {
                _state.value = state.value.copy(
                    editedItem = null
                )
            }
        }

        setProgress()
        saveChanges()
    }

    override fun getEditedItem(): Supplement? {
        return state.value.editedItem
    }

    fun setEditedItem(supplement: Supplement? = null) {
        //set given or default
        val _supplement = supplement ?: Supplement(
            id = -1,
            name = "",
            description = "",
            consumed = false,
            timeToConsume = LocalTime.now(),
            hasNotification = false
        )

        _state.value = state.value.copy(
            editedItem = _supplement
        )
    }

    private fun addSupplement(supplement: Supplement) {
        val list = state.value.supplements.toMutableList()
        list.add(supplement.copy(id = list.size))
        _state.value = state.value.copy(
            supplements = list
        )
    }

    private fun updateSupplement(supplement: Supplement) {
        val itemToUpdate = state.value.supplements.find { it.id == supplement.id }
        itemToUpdate?.let {
            val index = state.value.supplements.indexOf(itemToUpdate)
            val list = state.value.supplements.toMutableList()
            list[index] = itemToUpdate.copy(
                name = supplement.name,
                description = supplement.description
            )
            _state.value = state.value.copy(
                supplements = list
            )
        }
    }

    private fun removeSupplement(supplement: Supplement) {
        val list = state.value.supplements.toMutableList()
        list.remove(supplement)
        _state.value = state.value.copy(
            supplements = list
        )
    }

    fun onEditClick(supplement: Supplement) {
        _state.value = state.value.copy(
            editedItem = supplement
        )
    }

    private fun setProgress() {
        with(state.value) {
            _state.value = copy(
                progress = if (supplements.isNotEmpty()) {
                    (supplements.count { it.consumed } / supplements.size.toFloat())
                } else 0f
            )
        }
    }

    fun toggleConsumed(supplement: Supplement) {
        with(state.value) {
            val list = supplements.toMutableList()
            val indexToReplace = list.indexOf(supplement)
            if (indexToReplace in 0..list.lastIndex) {
                list[indexToReplace] = supplement.copy(consumed = !supplement.consumed)
            }
            _state.value = copy(supplements = list)
        }
        setProgress()
        saveChanges()
    }

    fun toggleEditMode(index: Int?) {
        _state.value = state.value.copy(
            // if current editedItemIndex is equal to index = turn off edit mode
            configItemIndex = index.takeIf { it != state.value.configItemIndex },
            //when user left editMode, update items order
            supplements =
            if (index != null) state.value.supplements.sortedBy { it.timeToConsume }
            else state.value.supplements
        )
    }

    fun setConsumedTime(supplement: Supplement, hourDelta: Int, minDelta: Int) {
        with(state.value) {
            val list = supplements.toMutableList()
            val indexToReplace = list.indexOf(supplement)
            if (indexToReplace in 0..list.lastIndex) {
                list[indexToReplace] = supplement.copy(
                    timeToConsume = TimeValidator.validateTime(
                        time = supplement.timeToConsume,
                        hourDelta = hourDelta,
                        minDelta = minDelta
                    )
                )
            }
            _state.value = copy(supplements = list)
        }
        saveChanges()
    }

    fun setNotification(hasPermission: Boolean, supplement: Supplement) {
        viewModelScope.launch {
            if (hasPermission) {
                val newState = !supplement.hasNotification
                with(state.value) {
                    val list = supplements.toMutableList()
                    val indexToReplace = list.indexOf(supplement)
                    if (indexToReplace in 0..list.lastIndex) {
                        list[indexToReplace] = supplement.copy(hasNotification = newState)
                    }
                    _state.value = copy(supplements = list)

                    _updateNotification.emit(
                        NotificationState(
                            notification = Notification(
                                id = supplement.id,
                                name = supplement.name,
                                timeInMillis = supplement.timeToConsume.localTimeToEpochMillis(),
                                active = newState,
                            ),
                            hasNotificationsPermission = true
                        )
                    )

                }
            } else {
                _updateNotification.emit(
                    NotificationState(
                        notification = null,
                        hasNotificationsPermission = false
                    )
                )
            }
            saveChanges()
        }
    }

    private fun saveChanges() {
        saveSupplementation(
            DailySupplementation(state.value.date, state.value.supplements)
        )
    }


}