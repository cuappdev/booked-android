package com.example.booked.ui.screens

import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import com.google.android.gms.maps.model.MapStyleOptions
import androidx.compose.ui.graphics.ColorFilter
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.booked.BuildConfig
import com.example.booked.R
import com.example.booked.RoomCard
import com.example.booked.data.model.StudyRoom
import com.example.booked.ui.viewmodels.HomeViewModel
import com.example.booked.ui.viewmodels.HomeUiState
import com.example.booked.ui.viewmodels.MapUiState
import com.example.booked.ui.viewmodels.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapRoute(
    onHomeClick: () -> Unit,
    onRoomClick: (StudyRoom) -> Unit,
    viewModel: MapViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val searchState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val visibleIds = searchState.visibleRooms.map { it.id }.toSet()
    val filteredLibraries = uiState.libraries.mapNotNull { library ->
        library.copy(rooms = library.rooms.filter { it.id in visibleIds }).takeIf { it.rooms.isNotEmpty() }
    }
    MapScreen(
        uiState = uiState.copy(libraries = filteredLibraries),
        searchState = searchState,
        onSearchChange = homeViewModel::onSearchChange,
        onFiltersClick = { homeViewModel.onFiltersVisibilityChange(true) },
        onLibraryChange = viewModel::onLibraryChange,
        onHomeClick = onHomeClick,
        onRoomClick = onRoomClick
    )
    if (searchState.showFilters) {
        ModalBottomSheet(
            onDismissRequest = { homeViewModel.onFiltersVisibilityChange(false) },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            RoomFiltersContent(
                uiState = searchState.filters,
                onSelectionChange = homeViewModel::onFilterSelectionChange,
                onShowAllLibrariesChange = homeViewModel::onShowAllLibrariesChange,
                onClear = homeViewModel::onClearFilters,
                onApply = homeViewModel::onApplyFilters
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MapScreen(
    uiState: MapUiState,
    searchState: HomeUiState,
    onSearchChange: (String) -> Unit,
    onFiltersClick: () -> Unit,
    onLibraryChange: (String) -> Unit,
    onHomeClick: () -> Unit,
    onRoomClick: (StudyRoom) -> Unit
) {
    val green = MaterialTheme.colorScheme.primary
    val cream = MaterialTheme.colorScheme.background
    val palette = MaterialTheme.colorScheme
    val mapStyle = remember(palette) {
        if (palette.background.luminance() >= 0.5f) null else {
            val background = "#%06X".format(palette.background.toArgb() and 0xFFFFFF)
            val surface = "#%06X".format(palette.surface.toArgb() and 0xFFFFFF)
            val text = "#%06X".format(palette.onSurface.toArgb() and 0xFFFFFF)
            val variant = "#%06X".format(palette.surfaceVariant.toArgb() and 0xFFFFFF)
            MapStyleOptions("""[
                {"elementType":"geometry","stylers":[{"color":"$background"}]},
                {"elementType":"labels.text.fill","stylers":[{"color":"$text"}]},
                {"elementType":"labels.text.stroke","stylers":[{"color":"$surface"}]},
                {"featureType":"road","elementType":"geometry","stylers":[{"color":"$variant"}]},
                {"featureType":"water","elementType":"geometry","stylers":[{"color":"$surface"}]}
            ]""")
        }
    }
    val selected = uiState.libraries.firstOrNull { it.name == uiState.selectedLibrary }
        ?: uiState.libraries.firstOrNull()
    val sheetState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    val expanded = sheetState.bottomSheetState.targetValue == SheetValue.Expanded
    val camera = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(42.4476, -76.4850), 16f)
    }
    var mapLoaded by remember { mutableStateOf(false) }
    BackHandler {
        if (expanded) scope.launch { sheetState.bottomSheetState.partialExpand() }
        else onHomeClick()
    }
    LaunchedEffect(selected?.name, mapLoaded) {
        if (mapLoaded && selected != null) {
            camera.animate(CameraUpdateFactory.newLatLngZoom(
                LatLng(selected.location.latitude, selected.location.longitude), 16.5f
            ))
        }
    }
    Scaffold(
        containerColor = cream,
        bottomBar = {
            Row(
                Modifier.fillMaxWidth().background(cream).navigationBarsPadding().height(64.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onHomeClick) {
                    Image(painterResource(R.drawable.map_home), "Home", Modifier.size(28.dp), colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = { scope.launch { sheetState.bottomSheetState.partialExpand() } }) {
                        Image(painterResource(R.drawable.map_selected), "Map, selected", Modifier.size(28.dp), colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary))
                    }
                    Box(Modifier.width(40.dp).height(1.dp).background(green))
                }

                IconButton(onClick = {}, enabled = false) {
                    Image(painterResource(R.drawable.profile), "Profile, unavailable", Modifier.size(28.dp), colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface))
                }
            }
        }
    ) { padding ->
        BoxWithConstraints(Modifier.fillMaxSize().padding(padding)) {
            val sheetHeight = maxHeight * 0.48f
            BottomSheetScaffold(
                scaffoldState = sheetState,
                sheetPeekHeight = 190.dp,
                sheetMaxWidth = if (expanded) 640.dp else maxWidth - 28.dp,
                sheetShape = if (expanded) RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp) else RoundedCornerShape(20.dp),
                sheetContainerColor = MaterialTheme.colorScheme.surface,
                sheetShadowElevation = 6.dp,
                sheetDragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.outlineVariant, width = 36.dp) },
                sheetContent = {
                    Column(Modifier.fillMaxWidth().height(sheetHeight).padding(horizontal = 20.dp)) {
                        if (selected == null) {
                            Text("No matching libraries", color = green, style = MaterialTheme.typography.titleLarge)
                            Text("Try another search or change your filters.", Modifier.padding(top = 8.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        } else {
                            Surface(
                                onClick = { scope.launch {
                                    if (expanded) sheetState.bottomSheetState.partialExpand()
                                    else sheetState.bottomSheetState.expand()
                                } },
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Row(Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                                    if (!expanded) {
                                        Image(
                                            painterResource(if (selected.name == "Uris Library") R.drawable.uris_library else selected.rooms.first().image),
                                            "${selected.name} preview",
                                            Modifier.width(102.dp).height(112.dp).clip(RoundedCornerShape(10.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text(selected.name, color = green, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Medium)
                                        Text("${selected.rooms.size} ${if (selected.rooms.size == 1) "space" else "spaces"} · ${selected.rooms.count { it.availableNow }} available now", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                                        val tags = (selected.rooms.map { it.reservation.label } +
                                            selected.rooms.map { it.spaceType.label } + selected.rooms.map { it.noise.label }).distinct().take(4)
                                        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                            tags.forEach { tag ->
                                                Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(50.dp)) {
                                                    Text(tag, Modifier.padding(horizontal = 10.dp, vertical = 7.dp), color = green, style = MaterialTheme.typography.labelSmall)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (expanded) LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(18.dp),
                                contentPadding = PaddingValues(top = 4.dp, bottom = 24.dp)
                            ) {
                                items(selected.rooms, key = { it.id }) { room ->
                                    RoomCard(room.name, room.library, room.collaborative, room.bookable, room.image,
                                        onClick = { onRoomClick(room) },
                                        spaceLabel = room.spaceType.label, noiseLabel = room.noise.label,
                                        reservationLabel = room.reservation.label)
                                }
                            }
                        }
                    }
                }
            ) {
                Box(Modifier.fillMaxSize()) {
                    if (BuildConfig.MAPS_CONFIGURED) {
                        GoogleMap(
                            Modifier.fillMaxSize(), cameraPositionState = camera,
                            properties = MapProperties(mapStyleOptions = mapStyle),
                            contentPadding = PaddingValues(top = 140.dp, bottom = 210.dp),
                            uiSettings = MapUiSettings(mapToolbarEnabled = false, zoomControlsEnabled = false),
                            onMapLoaded = { mapLoaded = true }
                        ) {
                            uiState.libraries.forEach { library ->
                                key(library.name) {
                                    val isSelected = library.name == selected?.name
                                    MarkerComposable(
                                        isSelected,
                                        state = rememberUpdatedMarkerState(LatLng(library.location.latitude, library.location.longitude)),
                                        title = library.name,
                                        onClick = { onLibraryChange(library.name); true }
                                    ) {
                                        Surface(
                                            modifier = Modifier.padding(4.dp),
                                            shape = RoundedCornerShape(18.dp),
                                            color = if (isSelected) green else MaterialTheme.colorScheme.surface,
                                            shadowElevation = 3.dp
                                        ) {
                                            Icon(painterResource(R.drawable.library_books), null,
                                                Modifier.padding(horizontal = 12.dp, vertical = 7.dp).size(24.dp),
                                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else green)
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Text("Map unavailable. Browse libraries below.", Modifier.align(Alignment.Center).padding(24.dp), color = green)
                    }
                    Column(
                        Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(cream, cream.copy(alpha = 0.95f), Color.Transparent)))
                            .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = searchState.search, onValueChange = onSearchChange,
                                placeholder = { Text("Where to book?", color = green.copy(alpha = 0.35f), style = MaterialTheme.typography.bodyLarge) },
                                leadingIcon = { Image(painterResource(R.drawable.search), null, Modifier.size(22.dp), colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)) },
                                modifier = Modifier.weight(1f), singleLine = true, shape = RoundedCornerShape(50.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.surface, unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                    focusedBorderColor = green, unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant, cursorColor = green
                                )
                            )
                            IconButton(onClick = onFiltersClick) {
                                BadgedBox(badge = { if (searchState.appliedFilters.isNotEmpty()) Badge(containerColor = green) { Text("${searchState.appliedFilters.size}") } }) {
                                    Image(painterResource(R.drawable.filter), "Open filters", Modifier.size(24.dp), colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface))
                                }
                            }
                        }
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(searchState.appliedFilters.ifEmpty { listOf("All") }) { filter ->
                                Surface(onClick = onFiltersClick, shape = RoundedCornerShape(50.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                                    Text(filter.substringAfter(":"), Modifier.padding(horizontal = 12.dp, vertical = 8.dp), color = green, style = MaterialTheme.typography.labelMedium)
                                }
                            }
                        }
                        if (!BuildConfig.MAPS_CONFIGURED) {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(uiState.libraries, key = { it.name }) { library ->
                                    FilterChip(selected = library.name == selected?.name, onClick = { onLibraryChange(library.name) }, label = { Text(library.name) })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
