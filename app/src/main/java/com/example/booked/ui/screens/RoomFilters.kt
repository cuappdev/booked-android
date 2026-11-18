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
            Text("Filters", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary)
            TextButton(onClick = onClear, enabled = uiState.selectedFilters.isNotEmpty()) {
                Text("Clear all", color = if (uiState.selectedFilters.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary)
            }
        }

        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            uiState.sections.forEach { section ->
                Column {
                    Text(section.title, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyLarge,
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
                                label = { Text("${option.label} · ${option.count}", style = MaterialTheme.typography.labelMedium) },
                                shape = RoundedCornerShape(50),
                                border = if (selected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                                    else BorderStroke(0.dp, Color.Transparent),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    labelColor = MaterialTheme.colorScheme.primary,
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }
                    if (section.title == "Library" && uiState.canExpandLibraries) {
                        TextButton(
                            onClick = { onShowAllLibrariesChange(!uiState.showAllLibraries) },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(if (uiState.showAllLibraries) "See less" else "See more", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }

        Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 6.dp) {
            Button(
                onClick = onApply,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)
                    .heightIn(min = 52.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    if (uiState.resultCount == 1) "Show 1 room" else "Show ${uiState.resultCount} rooms",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
