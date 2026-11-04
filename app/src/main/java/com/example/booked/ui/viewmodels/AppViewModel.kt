package com.example.booked.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AppUiState(val showSplash: Boolean = true, val showHome: Boolean = false)

@HiltViewModel
class AppViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _uiState = MutableStateFlow(
        AppUiState(
            showSplash = savedStateHandle["showSplash"] ?: true,
            showHome = savedStateHandle["showHome"] ?: false
        )
    )
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        if (_uiState.value.showSplash) {
            viewModelScope.launch {
                delay(2_000)
                savedStateHandle["showSplash"] = false
                _uiState.value = _uiState.value.copy(showSplash = false)
            }
        }
    }

    fun onSignInClick() {
        savedStateHandle["showHome"] = true
        _uiState.value = _uiState.value.copy(showHome = true)
    }
}
