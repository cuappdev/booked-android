package com.example.booked

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RoomCard(roomName: String, libraryName: String, collaborative: Boolean, bookable: Boolean, image: Int)
{
    Card(
        modifier = Modifier.width(248.87.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEFDFC)),
        elevation = CardDefaults.cardElevation(defaultElevation=4.dp)
    )
    {
        Box(modifier=Modifier.fillMaxWidth()
            .height(150.dp))
        {
            Image(painter=painterResource(image),
                contentDescription=libraryName,
                contentScale= ContentScale.Crop,
                modifier=Modifier
                    .fillMaxSize())
            IconButton(onClick = {},
                modifier=Modifier.align(Alignment.TopEnd)
                    .padding(15.dp)
                    .size(35.dp)
                    .background(color=Color(0xFFFEFDFB), shape= CircleShape))
            {
                Icon(
                    painter = painterResource(R.drawable.heart),
                    contentDescription = "Favorite",
                    tint=Color(0xFF595C5A),
                    modifier=Modifier.size(20.dp))

            }
        }
        Column(modifier=Modifier
                .fillMaxWidth()
                .padding(horizontal=16.dp, vertical=16.dp))
        {
            Text(text=roomName,
                fontSize=18.sp,
                color=Color(0xFF3F4F44),
                maxLines=1,
                overflow=TextOverflow.Ellipsis)
            Spacer(Modifier.height(5.dp))

            Row {
                Text(
                    text = libraryName,
                    fontSize = 14.sp,
                    color = Color(0xFF686862)
                )
                Spacer(modifier=Modifier.width(4.dp))
                Text(text="·",
                    fontSize = 14.sp,
                    color = Color(0xFF686862))
                Spacer(modifier=Modifier.width(4.dp))
                Text(
                    text = if (bookable) "Bookable" else "By request",
                    fontSize = 14.sp,
                    color = if (bookable) Color(0xFF47B383) else Color(0xFF6B6A60)
                )
            }

            Spacer(Modifier.height(5.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.sofa),
                    contentDescription = "Individual study",
                    tint = Color.Gray,
                    modifier = Modifier.size(13.dp)
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    text = "Individual Study",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.volume),
                    contentDescription = if (collaborative) "Collaborative" else "Silent",
                    tint = Color.Gray,
                    modifier = Modifier.size(13.dp)
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    text = if (collaborative) "Collaborative" else "Silent",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewRoomCard() {
    RoomCard(
        roomName = "4B02 Cocktail",
        libraryName = "Uris Library",
        collaborative = true,
        bookable = true,
        image = R.drawable.uris
    );
}
