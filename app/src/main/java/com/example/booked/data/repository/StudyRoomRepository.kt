package com.example.booked.data.repository

import com.example.booked.R
import com.example.booked.data.model.StudyRoom
import javax.inject.Inject

interface StudyRoomRepository {
    fun getRooms(): List<StudyRoom>
}

class SampleStudyRoomRepository @Inject constructor() : StudyRoomRepository {
    override fun getRooms(): List<StudyRoom> = (2..5).map { number ->
        StudyRoom(
            name = "4B0$number Cocktail Lounge",
            library = "Uris Library",
            collaborative = true,
            bookable = true,
            availableNow = true,
            image = R.drawable.uris
        )
    }
}
