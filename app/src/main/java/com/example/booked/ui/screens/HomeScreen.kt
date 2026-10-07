package com.example.booked.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.booked.R
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.tooling.preview.Preview
import com.example.booked.RoomCard

@Composable
fun HomeScreen(onLocationClick: () -> Unit = {},
               onProfileClick: () -> Unit = {})
{
    val sampleRooms = listOf(StudyRoom("4B02 Cocktail Lounge",
        "Uris Library",
        collaborative = true,
        bookable = true,
        availableNow = true,
        image = R.drawable.uris), StudyRoom("4B03 Cocktail Lounge",
        "Uris Library",
        collaborative = true,
        bookable = true,
        availableNow = true,
        image = R.drawable.uris),
        StudyRoom("4B04 Cocktail Lounge",
            "Uris Library",
            collaborative = true,
            bookable = true,
            availableNow = true,
            image = R.drawable.uris),
        StudyRoom("4B05 Cocktail Lounge",
            "Uris Library",
            collaborative = true,
            bookable = true,
            availableNow = true,
            image = R.drawable.uris))

    val background = Color(0xFFFAF7F1)
    val green = Color(0xFF3F4F44)
    var search by remember { mutableStateOf("") }
    val visibleRooms = sampleRooms.filter { room ->
        room.name.contains(search, ignoreCase = true) ||
                room.library.contains(search, ignoreCase = true)
    }

    Scaffold(
        containerColor = background,
        bottomBar = {
            Surface(
                color = background,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .height(64.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {}) {
                        Image(painter = painterResource(R.drawable.home_filled),
                            contentDescription = "Home",
                            modifier = Modifier.size(40.dp))
                    }

                    IconButton(onClick = onLocationClick) {
                        Image(
                            painter = painterResource(R.drawable.nav),
                            contentDescription = "Location",
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    IconButton(onClick = onProfileClick) {
                        Image(painter = painterResource(R.drawable.profile),
                            contentDescription="",
                            modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo_green),
                contentDescription = "Booked",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 20.dp, bottom = 18.dp)
                    .size(32.dp)
            )

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                placeholder = {
                    Text(
                        text = "Where to book?",
                        color = Color(0xFFB8BDB7),
                        fontSize = 16.sp
                    )
                },
                leadingIcon = { Image(painter = painterResource(R.drawable.search),
                    contentDescription =null,
                    modifier = Modifier.size(22.dp))},
                singleLine = true,
                shape = RoundedCornerShape(50.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = green,
                    unfocusedBorderColor = Color(0xFFE5E6DF),
                    focusedTextColor = green,
                    unfocusedTextColor = green,
                    cursorColor = green
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Surface(
                color = Color(0xFFDDECE3),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(
                    text = "Available Now",
                    fontSize = 12.sp,
                    color = green,
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    )
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Available now",
                fontSize = 18.sp,
                color = Color(0xFF595C5A)
            )

            Spacer(Modifier.height(16.dp))

            if (visibleRooms.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No rooms match your search.",
                        color = Color(0xFF686862)
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    items(
                        items = visibleRooms,
                        key = { it.name }
                    ) { room ->
                        RoomCard(
                            roomName = room.name,
                            libraryName = room.library,
                            collaborative = room.collaborative,
                            bookable = room.bookable,
                            image = room.image
                        )
                    }
                }
            }
        }
    }

}

data class StudyRoom(
    val name: String,
    val library: String,
    val collaborative: Boolean,
    val bookable: Boolean,
    val availableNow: Boolean,
    val image: Int
)

@Preview
@Composable
fun HomeScreenPreview()
{
    HomeScreen()
}