package io.suppline.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.suppline.domain.Config
import io.suppline.domain.preferences.Preferences
import javax.inject.Inject

@HiltViewModel
class SuppLineViewModel @Inject constructor(
    private val preferences: Preferences,
): ViewModel() {

    private var _state = mutableStateOf(SuppLineState(Config.DEFAULT_SUPPLEMENTS))
    val state: State<SuppLineState> = _state

    init {
        fetchData()
    }

    private fun fetchData() {
        _state.value = state.value.copy(
            dailySupplements = preferences.loadDailySupplements() ?: Config.DEFAULT_SUPPLEMENTS
        )
    }




}