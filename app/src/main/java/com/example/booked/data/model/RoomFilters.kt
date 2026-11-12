package com.example.booked.data.model

internal val FilterGroups = listOf("Library", "Reservation", "Noise Level", "Space Type")

internal fun StudyRoom.filterValue(group: String): String = when (group) {
    "Library" -> library
    "Reservation" -> reservation.label
    "Noise Level" -> noise.label
    "Space Type" -> spaceType.label
    else -> error("Unknown filter group: $group")
}

// Choices within a category are OR; different categories are AND.
internal fun StudyRoom.matchesFilters(selections: List<String>): Boolean =
    FilterGroups.all { group ->
        val choices = selections.filter { it.startsWith("$group:") }
        choices.isEmpty() || "$group:${filterValue(group)}" in choices
    }

