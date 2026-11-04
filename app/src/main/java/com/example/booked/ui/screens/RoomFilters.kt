package com.example.booked.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.booked.ui.viewmodels.FiltersUiState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val FilterGreen = Color(0xFF3F4F44)
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun RoomFiltersContent(
    uiState: FiltersUiState,
    onSelectionChange: (String) -> Unit,
    onShowAllLibrariesChange: (Boolean) -> Unit,
    onClear: () -> Unit,
    onApply: () -> Unit
) {
    Column(
        Modifier.fillMaxWidth().fillMaxHeight(0.85f)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Filters", fontSize = 22.sp, fontWeight = FontWeight.SemiBold,
                color = FilterGreen)
            TextButton(onClick = onClear, enabled = uiState.selectedFilters.isNotEmpty()) {
                Text("Clear all", color = if (uiState.selectedFilters.isEmpty()) Color.Gray else FilterGreen)
            }
        }

        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            uiState.sections.forEach { section ->
                Column {
                    Text(section.title, color = Color(0xFF595C5A), fontSize = 16.sp,
                        fontWeight = FontWeight.Medium)
                    Spacer(Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        section.options.forEach { option ->
                            val selected = option.key in uiState.selectedFilters
                            FilterChip(
                                selected = selected,
                                onClick = { onSelectionChange(option.key) },
                                label = { Text("${option.label} · ${option.count}", fontSize = 12.sp) },
                                shape = RoundedCornerShape(50),
                                border = if (selected) BorderStroke(1.dp, FilterGreen)
                                    else BorderStroke(0.dp, Color.Transparent),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Color(0xFFECEDE7),
                                    labelColor = FilterGreen,
                                    selectedContainerColor = Color(0xFFDDECE3),
                                    selectedLabelColor = FilterGreen
                                )
                            )
                        }
                    }
                    if (section.title == "Library" && uiState.canExpandLibraries) {
                        TextButton(
                            onClick = { onShowAllLibrariesChange(!uiState.showAllLibraries) },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(if (uiState.showAllLibraries) "See less" else "See more", color = FilterGreen)
                        }
                    }
                }
            }
        }

        Surface(color = Color(0xFFFEFDFC), shadowElevation = 6.dp) {
            Button(
                onClick = onApply,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)
                    .heightIn(min = 52.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = FilterGreen)
            ) {
                Text(
                    if (uiState.resultCount == 1) "Show 1 room" else "Show ${uiState.resultCount} rooms",
                    fontSize = 16.sp
                )
            }
        }
    }
}
