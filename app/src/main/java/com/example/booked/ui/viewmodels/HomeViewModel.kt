package com.example.booked.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.booked.data.model.FilterGroup
import com.example.booked.data.model.NoiseLevel
import com.example.booked.data.model.ReservationType
import com.example.booked.data.model.SpaceType
import com.example.booked.data.model.StudyRoom
import com.example.booked.data.model.filterValue
import com.example.booked.data.model.matchesFilters
import com.example.booked.data.repository.StudyRoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FilterOption(val key: String, val label: String, val count: Int)
data class FilterSection(val title: String, val options: List<FilterOption>)
data class FiltersUiState(
    val sections: List<FilterSection> = emptyList(),
    val selectedFilters: List<String> = emptyList(),
    val resultCount: Int = 0,
    val showAllLibraries: Boolean = false,
    val canExpandLibraries: Boolean = false
)
data class HomeUiState(
    val search: String = "",
    val visibleRooms: List<StudyRoom> = emptyList(),
    val appliedFilters: List<String> = emptyList(),
    val showFilters: Boolean = false,
    val filters: FiltersUiState = FiltersUiState()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: StudyRoomRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val rooms = repository.getRooms()
    enum class Library(val label: String)
    {
        MANN("Mann Library"),
        OLIN("Olin Library"),
        URIS("Uris Library"),
        CATHERWOOD("Catherwood Library"),
        ENGINEERING("Engineering Library")
    }
    private val categories = linkedMapOf(
        FilterGroup.LIBRARY to (Library.entries.map { it.label} + rooms.map { it.library }).distinct(),
        FilterGroup.RESERVATION to ReservationType.entries.map { it.label },
        FilterGroup.NOISE_LEVEL to NoiseLevel.entries.map { it.label },
        FilterGroup.SPACE_TYPE to SpaceType.entries.map { it.label }
    )
    private val validKeys = categories.flatMap { (group, labels) -> labels.map { "${group.label}:$it" } }.toSet()
    private var searchQuery = savedStateHandle.get<String>("search").orEmpty()
    private var applied = restoredFilters("appliedFilters")
    private var showFilters = savedStateHandle["showFilters"] ?: false
    private var draft = if (showFilters) restoredFilters("draftFilters") else applied
    private var showAllLibraries = savedStateHandle["showAllLibraries"] ?: false
    private val _uiState = MutableStateFlow(buildUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onSearchChange(value: String) {
        searchQuery = value
        publish()
    }

    fun onFiltersVisibilityChange(visible: Boolean) {
        if (visible != showFilters) draft = applied.toList()
        showFilters = visible
        publish()
    }

    fun onFilterSelectionChange(key: String) {
        if (!showFilters || key !in validKeys) return
        draft = if (key in draft) draft - key else draft + key
        publish()
    }

    fun onShowAllLibrariesChange(expanded: Boolean) {
        showAllLibraries = expanded
        publish()
    }

    fun onClearFilters() {
        if (!showFilters) return
        draft = emptyList()
        publish()
    }

    fun onApplyFilters() {
        if (!showFilters) return
        applied = draft.toList()
        showFilters = false
        publish()
    }

    private fun restoredFilters(key: String): List<String> =
        savedStateHandle.get<ArrayList<String>>(key).orEmpty().filter { it in validKeys }.distinct()

    private fun publish() {
        savedStateHandle["search"] = searchQuery
        savedStateHandle["appliedFilters"] = ArrayList(applied)
        savedStateHandle["draftFilters"] = ArrayList(draft)
        savedStateHandle["showFilters"] = showFilters
        savedStateHandle["showAllLibraries"] = showAllLibraries
        _uiState.value = buildUiState()
    }

    private fun buildUiState(): HomeUiState {
        val matchingSearch = rooms.filter {
            it.name.contains(searchQuery, ignoreCase = true) || it.library.contains(searchQuery, ignoreCase = true)
        }
        val sections = categories.map { (group, labels) ->
            val otherSelections = draft.filterNot { it.startsWith("${group.label}:") }
            val countedRooms = matchingSearch.filter { it.matchesFilters(otherSelections) }
            val visibleLabels = if (group == FilterGroup.LIBRARY && !showAllLibraries) labels.take(5) else labels
            FilterSection(group.label, visibleLabels.map { label ->
                FilterOption("${group.label}:$label", label, countedRooms.count { it.filterValue(group) == label })
            })
        }
        return HomeUiState(
            search = searchQuery,
            visibleRooms = matchingSearch.filter { it.matchesFilters(applied) },
            appliedFilters = applied,
            showFilters = showFilters,
            filters = FiltersUiState(
                sections = sections,
                selectedFilters = draft,
                resultCount = matchingSearch.count { it.matchesFilters(draft) },
                showAllLibraries = showAllLibraries,
                canExpandLibraries = categories.getValue(FilterGroup.LIBRARY).size > 5
            )
        )
    }
}
