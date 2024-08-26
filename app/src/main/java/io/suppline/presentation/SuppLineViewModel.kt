package io.suppline.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.suppline.domain.Config
import io.suppline.domain.models.DailySupplementation
import io.suppline.domain.models.Supplement
import io.suppline.domain.preferences.Preferences
import io.suppline.domain.utils.validators.TimeValidator
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class SuppLineViewModel @Inject constructor(
    private val preferences: Preferences,
) : ViewModel() {

    private var _state = mutableStateOf(SuppLineState())
    val state: State<SuppLineState> = _state

    init {
        fetchData()
    }

    private fun fetchData() {
        //fetch from preferences or create new based on default
        val supplementation = preferences.loadDailySupplements() ?: kotlin.run {
            DailySupplementation(
                date = LocalDate.now().plusDays(9),
                supplements = Config.DEFAULT_SUPPLEMENT_NAMES.mapIndexed { index, s ->
                    Supplement(
                        id = index,
                        name = s,
                        consumed = false,
                        timeToConsume = LocalTime.now()
                    )
                }
            )
        }
        _state.value = state.value.copy(
            date = supplementation.date,
            supplements = supplementation.supplements
        )
        setProgress()
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

    fun toggleEditMode() {
        val isEditMode = !state.value.isEditMode
        _state.value = state.value.copy(
            isEditMode = isEditMode,
            //when user left editMode, update items order
            supplements =
            if (!isEditMode) state.value.supplements.sortedBy { it.timeToConsume }
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

    private fun saveChanges() {
        preferences.saveDailySupplementation(
            DailySupplementation(state.value.date, state.value.supplements)
        )
    }


}