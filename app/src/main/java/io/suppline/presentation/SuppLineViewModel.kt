package io.suppline.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.suppline.domain.models.DailySupplementation
import io.suppline.domain.models.Supplement
import io.suppline.domain.preferences.Preferences
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
                val supplement = event.supplement

                val list = state.value.supplements.toMutableList()
                val index = list.indexOf(supplement)
                list[index] = supplement.copy(name = event.name)

                _state.value = state.value.copy(
                    supplements = list
                )
            }

            is AddEditSupplementEvent.OnDescriptionChange -> {
                val supplement = event.supplement

                val list = state.value.supplements.toMutableList()
                val index = list.indexOf(supplement)
                list[index] = supplement.copy(description = event.description)

                _state.value = state.value.copy(
                    supplements = list
                )
            }
        }
    }

    override fun getEditedItem(): Supplement {
        val index = state.value.editedItemIndex
        val editedItem = index?.let {
            state.value.supplements.getOrNull(it)
        }
        //get or create new supplement
        return editedItem!!
    }

    private fun addSupplement(): Supplement {
        val supplement = Supplement(
            id = state.value.supplements.size,
            name = "",
            description = "",
            consumed = false,
            timeToConsume = LocalTime.now(),
            hasNotification = false
        )
        val updatedList = state.value.supplements.toMutableList()
        updatedList.add(supplement)

        _state.value = state.value.copy(
            supplements = updatedList
        )

        return supplement
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
            editedItemIndex = index.takeIf { it != state.value.editedItemIndex },
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