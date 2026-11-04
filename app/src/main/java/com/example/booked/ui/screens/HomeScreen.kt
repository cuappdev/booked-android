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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.material3.IconButton
import androidx.compose.ui.tooling.preview.Preview
import com.example.booked.RoomCard
import com.example.booked.data.model.StudyRoom
import com.example.booked.ui.viewmodels.HomeViewModel
import com.example.booked.ui.viewmodels.HomeUiState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
@Composable
fun HomeRoute(
    onLocationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        uiState = uiState,
        onSearchChange = viewModel::onSearchChange,
        onFiltersVisibilityChange = viewModel::onFiltersVisibilityChange,
        onFilterSelectionChange = viewModel::onFilterSelectionChange,
        onShowAllLibrariesChange = viewModel::onShowAllLibrariesChange,
        onClearFilters = viewModel::onClearFilters,
        onApplyFilters = viewModel::onApplyFilters,
        onLocationClick = onLocationClick,
        onProfileClick = onProfileClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onSearchChange: (String) -> Unit,
    onFiltersVisibilityChange: (Boolean) -> Unit,
    onFilterSelectionChange: (String) -> Unit,
    onShowAllLibrariesChange: (Boolean) -> Unit,
    onClearFilters: () -> Unit,
    onApplyFilters: () -> Unit,
    onLocationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val background = Color(0xFFFAF7F1)
    val green = Color(0xFF3F4F44)
    val filterSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

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
                            contentDescription = "Profile",
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiState.search,
                    onValueChange = onSearchChange,
                    placeholder = {
                        Text(
                            text = "Where to book?",
                            color = Color(0xFFB8BDB7),
                            fontSize = 16.sp
                        )
                    },
                    leadingIcon = { Image(painter = painterResource(R.drawable.search),
                        contentDescription = "Search rooms",
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
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { onFiltersVisibilityChange(true) },
                    modifier = Modifier.padding(start = 8.dp).size(48.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.filter),
                        contentDescription = "Open filters",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }


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

            if (uiState.visibleRooms.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No rooms match your search and filters.",
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
                        items = uiState.visibleRooms,
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
        if (uiState.showFilters) {
            ModalBottomSheet(
                onDismissRequest = { onFiltersVisibilityChange(false) },
                sheetState = filterSheetState,
                containerColor = Color(0xFFFEFDFC)
            ) {
                RoomFiltersContent(
                    uiState = uiState.filters,
                    onSelectionChange = onFilterSelectionChange,
                    onShowAllLibrariesChange = onShowAllLibrariesChange,
                    onClear = onClearFilters,
                    onApply = onApplyFilters
                )
            }
        }
    }

}

@Preview
@Composable
fun HomeScreenPreview()
{
    HomeScreen(
        uiState = HomeUiState(visibleRooms = listOf(
            StudyRoom(
                name = "4B02 Cocktail Lounge",
                library = "Uris Library",
                collaborative = true,
                bookable = true,
                availableNow = true,
                image = R.drawable.uris
            )
        )),
        onSearchChange = {},
        onFiltersVisibilityChange = {},
        onFilterSelectionChange = {},
        onShowAllLibrariesChange = {},
        onClearFilters = {},
        onApplyFilters = {}
    )
}
