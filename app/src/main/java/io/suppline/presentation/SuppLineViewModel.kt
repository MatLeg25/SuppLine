package io.suppline.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.suppline.domain.Config
import io.suppline.domain.models.DailySupplementation
import io.suppline.domain.models.Supplement
import io.suppline.domain.preferences.Preferences
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SuppLineViewModel @Inject constructor(
    private val preferences: Preferences,
): ViewModel() {

    private var _state = mutableStateOf(SuppLineState())
    val state: State<SuppLineState> = _state

    init {
        preferences.clear()
        println(">>>>>>>>> clear")
        val defaultSupplements = Config.DEFAULT_SUPPLEMENTS
        preferences.saveDailySupplementation(
            DailySupplementation(
                date = LocalDate.now(),
                supplements = defaultSupplements.associateWith { true }
            )
        )
        fetchData()
    }

    private fun fetchData() {
        println(">>>>>>>>> fetchData start")
        val supplementation = preferences.loadDailySupplements() ?: kotlin.run {
            val defaultSupplements = Config.DEFAULT_SUPPLEMENTS
            DailySupplementation(
                date = LocalDate.now(),
                supplements = defaultSupplements.associateWith { false }
            )
        }
        println(">>>>>>>>> fetchData = $supplementation")
        _state.value = state.value.copy(
            supplementsMap = supplementation.supplements
        )
    }

    private fun setProgress() {
        with(state.value) {
            _state.value = copy(
                progress = if (supplementsMap.isNotEmpty()) {
                    (supplementsMap.values.count { it } / supplementsMap.size.toFloat())
                } else 0f
            )
        }
    }

    fun toggleConsumed(supplement: Supplement) {
        with(state.value) {
            val map = supplementsMap.toMutableMap()
            map[supplement] = !map.getOrDefault(supplement, false)
            _state.value = copy(supplementsMap = map)
        }
        setProgress()
    }


}