package com.example.booked.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.booked.data.model.StudyRoom
import com.example.booked.data.repository.StudyRoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class RoomDetailUiState(
    val room: StudyRoom? = null,
    val isFavorite: Boolean = false
)

@HiltViewModel
class RoomDetailViewModel @Inject constructor(
    private val repository: StudyRoomRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(RoomDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        savedStateHandle.get<String>("roomId")?.let(::onRoomChange)
    }

    fun onRoomChange(roomId: String) {
        savedStateHandle["roomId"] = roomId
        val favorites = savedStateHandle.get<ArrayList<String>>("favorites").orEmpty()
        _uiState.value = RoomDetailUiState(
            room = repository.getRooms().firstOrNull { it.id == roomId },
            isFavorite = roomId in favorites
        )
    }

    fun onFavoriteChange() {
        val room = _uiState.value.room ?: return
        val favorites = savedStateHandle.get<ArrayList<String>>("favorites").orEmpty().toMutableSet()
        if (!favorites.add(room.id)) favorites.remove(room.id)
        savedStateHandle["favorites"] = ArrayList(favorites)
        _uiState.value = _uiState.value.copy(isFavorite = room.id in favorites)
    }
}
