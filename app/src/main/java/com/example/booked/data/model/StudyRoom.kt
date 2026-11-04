package com.example.booked.data.model

enum class ReservationType(val label: String) {
    BOOKABLE("Bookable"),
    BY_REQUEST("By request"),
    WALK_IN("Walk-in")
}

enum class NoiseLevel(val label: String) {
    SILENT("Silent"),
    QUIET("Quiet"),
    COLLABORATIVE("Collaborative")
}

enum class SpaceType(val label: String) {
    OPEN_STUDY("Open Study"),
    GROUP_STUDY("Group Study"),
    INDIVIDUAL_STUDY("Individual Study"),
    READING_ROOM("Reading Room"),
    CLASSROOM("Classroom"),
    SEMINAR("Seminar")
}
data class StudyRoom(
    val name: String,
    val library: String,
    val collaborative: Boolean,
    val bookable: Boolean,
    val availableNow: Boolean,
    val image: Int,
    val reservation: ReservationType = if (bookable)
        ReservationType.BOOKABLE else ReservationType.BY_REQUEST,
    val noise: NoiseLevel = if (collaborative)
        NoiseLevel.COLLABORATIVE else NoiseLevel.SILENT,
    val spaceType: SpaceType = SpaceType.INDIVIDUAL_STUDY
)

