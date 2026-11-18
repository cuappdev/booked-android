package com.example.booked.data.model

enum class FilterGroup(val label: String)
{
    LIBRARY("Library"),
    RESERVATION("Reservation"),
    NOISE_LEVEL("Noise Level"),
    SPACE_TYPE("Space Type")
}

internal fun StudyRoom.filterValue(group: FilterGroup): String =
    when (group) {
    FilterGroup.LIBRARY -> library
    FilterGroup.RESERVATION -> reservation.label
    FilterGroup.NOISE_LEVEL -> noise.label
    FilterGroup.SPACE_TYPE -> spaceType.label
}

internal fun StudyRoom.matchesFilters(selections: List<String>): Boolean =
    FilterGroup.entries.all { group ->
        val prefix = "${group.label}:"
        val choices = selections.filter { it.startsWith(prefix) }
        choices.isEmpty() || "$prefix${filterValue(group)}" in choices
    }

