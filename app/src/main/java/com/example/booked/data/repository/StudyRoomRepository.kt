package com.example.booked.data.repository

import com.example.booked.R
import com.example.booked.data.model.RoomLocation
import com.example.booked.data.model.StudyRoom
import com.example.booked.data.model.NoiseLevel
import com.example.booked.data.model.SpaceType
import javax.inject.Inject

interface StudyRoomRepository {
    fun getRooms(): List<StudyRoom>
}

class SampleStudyRoomRepository @Inject constructor() : StudyRoomRepository {
    override fun getRooms(): List<StudyRoom> = listOf(

        StudyRoom(
            name = "120B Group Study", library = "Olin Library",
            collaborative = true, bookable = true, availableNow = false,
            image = R.drawable.olin_120b, noise = NoiseLevel.QUIET,
            spaceType = SpaceType.GROUP_STUDY,
            description = "Group study room. Seats 4. Only available after 6pm M-F. Available all day on weekends.",
            openingNote = "Opens 6pm Tues", foodNearby = true, hasMonitor = true,
            mapImage = R.drawable.olin_map,
            location = RoomLocation(42.4477, -76.4846)
        )
    ) + (2..5).map { number ->
        StudyRoom(
            name = "4B0$number Cocktail Lounge",
            library = "Uris Library",
            collaborative = true,
            bookable = true,
            availableNow = true,
            image = R.drawable.uris,
            location = RoomLocation(42.4475, -76.4855)
        )
    }
}
