package io.suppline.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.suppline.domain.Config
import io.suppline.domain.models.Supplement
import io.suppline.domain.preferences.Preferences
import javax.inject.Inject

@HiltViewModel
class SuppLineViewModel @Inject constructor(
    private val preferences: Preferences,
): ViewModel() {

    private var _state = mutableStateOf(SuppLineState())
    val state: State<SuppLineState> = _state

    init {
        fetchData()
    }

    private fun fetchData() {
        val supplements = preferences.loadDailySupplements() ?: Config.DEFAULT_SUPPLEMENTS
        _state.value = state.value.copy(
            supplementsMap = supplements.associateWith { false } //todo load state from memory
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