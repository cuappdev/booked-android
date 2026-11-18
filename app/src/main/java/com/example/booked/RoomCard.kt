package com.example.booked

import com.example.booked.ui.theme.BookedTheme
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RoomCard(roomName: String, libraryName: String, collaborative: Boolean, bookable: Boolean, image: Int, onClick: () -> Unit = {},
    spaceLabel: String = "Individual Study",
    noiseLabel: String = if (collaborative) "Collaborative" else "Silent",
    reservationLabel: String = if (bookable) "Bookable" else "By request")
{
    Card(
        onClick = onClick,
        modifier = Modifier.width(248.87.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                    .background(color=MaterialTheme.colorScheme.surface, shape= CircleShape))
            {
                Icon(
                    painter = painterResource(R.drawable.heart),
                    contentDescription = "Favorite",
                    tint=MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier=Modifier.size(20.dp))

            }
        }
        Column(modifier=Modifier
                .fillMaxWidth()
                .padding(horizontal=16.dp, vertical=16.dp))
        {
            Text(text=roomName,
                style = MaterialTheme.typography.titleMedium,
                color=MaterialTheme.colorScheme.primary,
                maxLines=1,
                overflow=TextOverflow.Ellipsis)
            Spacer(Modifier.height(5.dp))

            Text(
                text = buildAnnotatedString {
                    append("$libraryName · ")
                    withStyle(SpanStyle(color = if (bookable) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant)) {
                        append(reservationLabel)
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(5.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.sofa),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(13.dp)
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    text = spaceLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.volume),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(13.dp)
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    text = noiseLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewRoomCard() {
    BookedTheme {
        RoomCard(
            roomName = "4B02 Cocktail",
            libraryName = "Uris Library",
            collaborative = true,
            bookable = true,
            image = R.drawable.uris
        );
    }
}
