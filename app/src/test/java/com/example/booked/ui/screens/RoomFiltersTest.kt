package com.example.booked.ui.screens

import com.example.booked.data.model.StudyRoom
import com.example.booked.data.model.matchesFilters
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RoomFiltersTest {
    private val room = StudyRoom(
        name = "Study room", library = "Uris Library", collaborative = false,
        bookable = true, availableNow = true, image = 0
    )

    @Test fun noSelectionsIncludesRoom() {
        assertTrue(room.matchesFilters(emptyList()))
    }

    @Test fun choicesInOneCategoryAreAlternatives() {
        assertTrue(room.matchesFilters(listOf("Library:Olin Library", "Library:Uris Library")))
    }

    @Test fun everySelectedCategoryMustMatch() {
        assertFalse(room.matchesFilters(listOf("Library:Uris Library", "Noise Level:Quiet")))
        assertTrue(room.matchesFilters(listOf("Library:Uris Library", "Noise Level:Silent")))
    }

    @Test fun reservationAndSpaceSelectionsAreRespected() {
        assertTrue(room.matchesFilters(listOf("Reservation:Bookable", "Space Type:Individual Study")))
        assertFalse(room.matchesFilters(listOf("Reservation:Walk-in")))
        assertFalse(room.matchesFilters(listOf("Space Type:Classroom")))
    }
}
