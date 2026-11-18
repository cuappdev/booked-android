package com.example.booked.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.booked.data.model.RoomLocation
import com.example.booked.data.model.StudyRoom
import com.example.booked.data.repository.StudyRoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MapLibrary(val name: String, val location: RoomLocation, val rooms: List<StudyRoom>)
data class MapUiState(
    val libraries: List<MapLibrary> = emptyList(),
    val selectedLibrary: String? = null
)

@HiltViewModel
class MapViewModel @Inject constructor(
    repository: StudyRoomRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val libraries = repository.getRooms()
        .filter { room ->
            val location = room.location
            location != null && location.latitude.isFinite() && location.longitude.isFinite() &&
                location.latitude in -90.0..90.0 && location.longitude in -180.0..180.0
        }
        .groupBy { it.library }
        .map { (name, rooms) -> MapLibrary(name, requireNotNull(rooms.first().location), rooms) }
    private val restoredSelection = savedStateHandle.get<String>("selectedLibrary")
    private val _uiState = MutableStateFlow(MapUiState(
        libraries = libraries,
        selectedLibrary = libraries.firstOrNull { it.name == restoredSelection }?.name ?: libraries.firstOrNull()?.name
    ))
    val uiState = _uiState.asStateFlow()

    fun onLibraryChange(name: String) {
        if (libraries.none { it.name == name }) return
        savedStateHandle["selectedLibrary"] = name
        _uiState.value = _uiState.value.copy(selectedLibrary = name)
    }
}
