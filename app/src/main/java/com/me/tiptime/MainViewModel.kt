package com.me.tiptime

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState
    fun changeSwitch(isRoundTip: Boolean) {
        _uiState.update { currentUIState ->
            currentUIState.copy(isRoundTip = isRoundTip)
        }
    }

    fun changeTipOptions(tipOption: Double) {
        _uiState.update { currentUiState ->
            currentUiState.copy(serviceState = tipOption)
        }
    }

    fun changeCost(cost: String) {
        val costOfService = cost.toDoubleOrNull()
        _uiState.update { currentUIState ->
            currentUIState.copy(costOfService = costOfService)
        }
    }
}

data class MainUiState(
    val costOfService: Double? = null,
    val serviceState: Double = 0.20,
    val isRoundTip: Boolean = false
)