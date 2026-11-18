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
import kotlin.time.Duration.Companion.seconds

data class AppUiState(val showSplash: Boolean = true, val showHome: Boolean = false, val selectedRoomId: String? = null, val showMap: Boolean = false)

@HiltViewModel
class AppViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _uiState = MutableStateFlow(
        AppUiState(
            showSplash = savedStateHandle["showSplash"] ?: true,
            showHome = savedStateHandle["showHome"] ?: false,
            selectedRoomId = savedStateHandle["selectedRoomId"],
            showMap = savedStateHandle["showMap"] ?: false
        )
    )
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        if (_uiState.value.showSplash) {
            viewModelScope.launch {
                delay(2.seconds)
                savedStateHandle["showSplash"] = false
                _uiState.value = _uiState.value.copy(showSplash = false)
            }
        }
    }

    fun onMapVisibilityChange(visible: Boolean) {
        savedStateHandle["showMap"] = visible
        _uiState.value = _uiState.value.copy(showMap = visible)
    }

    fun onRoomChange(roomId: String?) {
        savedStateHandle["selectedRoomId"] = roomId
        _uiState.value = _uiState.value.copy(selectedRoomId = roomId)
    }

    fun onSignInClick() {
        savedStateHandle["showHome"] = true
        _uiState.value = _uiState.value.copy(showHome = true)
    }
}
