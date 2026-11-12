package com.example.booked.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import com.example.booked.data.model.StudyRoom
import com.example.booked.data.repository.StudyRoomRepository
import org.junit.Assert.*
import org.junit.Test

class HomeViewModelTest {
    private val rooms = listOf(
        StudyRoom("Alpha", "Uris Library", false, true, true, 0),
        StudyRoom("Beta", "Olin Library", true, false, true, 0),
        StudyRoom("Gamma", "Uris Library", true, true, true, 0)
    )
    private val repository = object : StudyRoomRepository {
        override fun getRooms() = rooms
    }

    @Test fun draftOnlyChangesResultsAfterApplyAndDismissDiscardsChanges() {
        val vm = HomeViewModel(repository, SavedStateHandle())
        vm.onFiltersVisibilityChange(true)
        vm.onFilterSelectionChange("Library:Uris Library")
        assertEquals(3, vm.uiState.value.visibleRooms.size)
        assertEquals(2, vm.uiState.value.filters.resultCount)
        vm.onApplyFilters()
        assertEquals(listOf("Alpha", "Gamma"), vm.uiState.value.visibleRooms.map { it.name })
        assertFalse(vm.uiState.value.showFilters)
        vm.onFiltersVisibilityChange(true)
        vm.onClearFilters()
        vm.onFiltersVisibilityChange(false)
        vm.onFiltersVisibilityChange(true)
        assertEquals(listOf("Library:Uris Library"), vm.uiState.value.filters.selectedFilters)
    }

    @Test fun countsIgnoreOwnGroupButRespectOtherGroupsAndSearch() {
        val vm = HomeViewModel(repository, SavedStateHandle())
        vm.onFiltersVisibilityChange(true)
        vm.onFilterSelectionChange("Library:Uris Library")
        vm.onFilterSelectionChange("Noise Level:Collaborative")
        val libraries = vm.uiState.value.filters.sections.first().options.associate { it.label to it.count }
        assertEquals(1, libraries["Olin Library"])
        assertEquals(1, libraries["Uris Library"])
        assertEquals(0, libraries["Mann Library"])
        assertEquals(1, vm.uiState.value.filters.resultCount)
        vm.onSearchChange("GAM")
        assertEquals(listOf("Gamma"), vm.uiState.value.visibleRooms.map { it.name })
        assertEquals(1, vm.uiState.value.filters.resultCount)
        assertEquals(0, vm.uiState.value.filters.sections.first().options.first { it.label == "Olin Library" }.count)
    }

    @Test fun recreatingWithSavedValuesRestoresOpenDraftAndCommittedFiltersSeparately() {
        val saved = SavedStateHandle()
        val vm = HomeViewModel(repository, saved)
        vm.onFiltersVisibilityChange(true)
        vm.onFilterSelectionChange("Library:Uris Library")
        vm.onApplyFilters()
        vm.onSearchChange("a")
        vm.onFiltersVisibilityChange(true)
        vm.onFilterSelectionChange("Noise Level:Silent")
        vm.onShowAllLibrariesChange(true)
        val restored = HomeViewModel(repository, SavedStateHandle(saved.keys().associateWith { saved.get<Any>(it) }))
        assertEquals(vm.uiState.value, restored.uiState.value)
        restored.onFiltersVisibilityChange(false)
        assertEquals(listOf("Library:Uris Library"), restored.uiState.value.filters.selectedFilters)
        assertEquals(2, restored.uiState.value.visibleRooms.size)
    }

    @Test fun clearingAndApplyingRemovesCommittedFilters() {
        val vm = HomeViewModel(repository, SavedStateHandle())
        vm.onFiltersVisibilityChange(true)
        vm.onFilterSelectionChange("Library:Uris Library")
        vm.onApplyFilters()
        vm.onFiltersVisibilityChange(true)
        vm.onClearFilters()
        vm.onApplyFilters()
        assertEquals(rooms, vm.uiState.value.visibleRooms)
    }
}
