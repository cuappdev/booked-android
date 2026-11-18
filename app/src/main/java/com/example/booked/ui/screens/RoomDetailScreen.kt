package com.example.booked.ui.screens

import com.example.booked.ui.theme.BookedTheme
import androidx.compose.ui.graphics.ColorFilter
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.annotation.DrawableRes
import com.example.booked.R
import com.example.booked.data.model.ReservationType
import com.example.booked.data.repository.SampleStudyRoomRepository
import com.example.booked.ui.viewmodels.RoomDetailUiState
import com.example.booked.ui.viewmodels.RoomDetailViewModel
import kotlinx.coroutines.launch


@Composable
fun RoomDetailRoute(
    roomId: String,
    onBackClick: () -> Unit,
    viewModel: RoomDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(roomId) { viewModel.onRoomChange(roomId) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    BackHandler(onBack = onBackClick)

    fun openLink(uri: Uri) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (_: ActivityNotFoundException) {
            scope.launch { snackbar.showSnackbar("No app is available to open this link.") }
        }
    }

    RoomDetailScreen(
        uiState = state,
        onBackClick = onBackClick,
        onFavoriteChange = viewModel::onFavoriteChange,
        onBookClick = {
            val url = state.room?.bookingUrl
            if (url != null) openLink(Uri.parse(url))
            else scope.launch {
                snackbar.showSnackbar("Booking is not connected for this sample room yet.")
            }
        },
        onLocationClick = {
            state.room?.let {
                openLink(Uri.parse("geo:0,0?q=${Uri.encode(it.library + ", Cornell University")}"))
            }
        },
        snackbarHostState = snackbar
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RoomDetailScreen(
    uiState: RoomDetailUiState,
    onBackClick: () -> Unit,
    onFavoriteChange: () -> Unit,
    onBookClick: () -> Unit,
    onLocationClick: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val room = uiState.room
    if (room == null) {
        Column(Modifier.fillMaxSize().safeDrawingPadding().padding(28.dp)) {
            Text("Room unavailable", color = MaterialTheme.colorScheme.onSurface)
            TextButton(onClick = onBackClick) { Text("Back to rooms") }
        }
        return
    }
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant)) {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            BoxWithConstraints(Modifier.fillMaxWidth().height(333.dp).clipToBounds()) {

                Image(
                    painterResource(room.image), "Photo of ${room.name}",
                    Modifier.align(Alignment.TopCenter)
                        .requiredWidth(maxWidth * (566f / 402f)).requiredHeight(425.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Surface(
                modifier = Modifier.offset(y = (-28).dp).fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = MaterialTheme.colorScheme.surface, shadowElevation = 4.dp
            ) {
                Column(
                    Modifier.padding(28.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(room.name, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Medium)
                            Row {
                                Text(if (room.availableNow) "Available now" else "Closed", color = if (room.availableNow) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                                room.openingNote?.let { Text(" · $it", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium) }
                            }
                        }
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            val tags = listOfNotNull(
                                room.library,
                                if (room.reservation == ReservationType.BOOKABLE) "Reservable" else room.reservation.label,
                                if (room.foodNearby) "Food Nearby" else null
                            )
                            tags.forEach { tag ->
                                Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(20.dp)) {
                                    Text(
                                        text = tag,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                        }
                    }
                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    Text(room.description.ifBlank { "${room.spaceType.label} at ${room.library}." }, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Features", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            DetailFeature(R.drawable.quiet, room.noise.label)
                            DetailFeature(R.drawable.group_study, if (room.spaceType.label == "Group Study") "Group Study Room" else room.spaceType.label)
                            if (room.collaborative) DetailFeature(R.drawable.collab_space, "Collaborative Space")
                            if (room.hasMonitor) DetailFeature(R.drawable.monitor, "Monitor/Zoom")
                        }
                    }
                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Location", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
                        Text(room.library, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                        room.mapImage?.let {
                            Image(painterResource(it), "Open map for ${room.library}",
                                Modifier.fillMaxWidth().height(192.dp).clip(RoundedCornerShape(20.dp)).clickable(onClick = onLocationClick),
                                contentScale = ContentScale.Crop)
                        } ?: TextButton(onClick = onLocationClick) { Text("Open in maps", color = MaterialTheme.colorScheme.primary) }
                    }
                    Spacer(Modifier.height(90.dp).navigationBarsPadding())
                }
            }
        }
        Row(
            Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 18.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBackClick, modifier = Modifier.size(48.dp)) {
                Box(
                    modifier = Modifier.size(35.dp).background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.back),
                        contentDescription = "Back to rooms",
                        modifier = Modifier.size(25.dp), colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface))
                }
            }
            IconButton(onClick = onFavoriteChange, modifier = Modifier.size(48.dp)) {
                Box(
                    modifier = Modifier.size(35.dp).background(
                        if (uiState.isFavorite) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        CircleShape
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.favorite),
                        contentDescription = if (uiState.isFavorite) "Remove from favorites" else "Save room",
                        modifier = Modifier.size(25.dp), colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface))
                }
            }
        }
        Column(
            Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                .background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.surface.copy(alpha = 0.05f), MaterialTheme.colorScheme.surface.copy(alpha = 0.6f), MaterialTheme.colorScheme.surface)))
                .navigationBarsPadding().padding(start = 20.dp, end = 20.dp, top = 40.dp, bottom = 28.dp)
        ) {
            Button(
                onClick = onBookClick,
                modifier = Modifier.fillMaxWidth().heightIn(min = 50.dp),
                shape = RoundedCornerShape(95.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) { Text("Book Now", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimary) }
        }
        SnackbarHost(snackbarHostState, Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(bottom = 100.dp))
    }
}

@Composable
private fun DetailFeature(@DrawableRes icon: Int, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(18.dp), colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface))
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(widthDp = 402, heightDp = 874, showBackground = true)
@Composable
private fun RoomDetailPreview() {
    BookedTheme {
        RoomDetailScreen(RoomDetailUiState(SampleStudyRoomRepository().getRooms().first()), {}, {}, {}, {})
    }
}
