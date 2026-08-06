package com.kabo.a24_makany.ui.screens.places

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kabo.a24_makany.data.local.PlaceEntity
import com.kabo.a24_makany.ui.components.MakanySearchBar
import com.kabo.a24_makany.ui.components.PlaceCard
import com.kabo.a24_makany.ui.screens.sheets.PlaceDetailesSheet
import androidx.core.net.toUri
import com.google.android.libraries.places.api.model.kotlin.place
import com.kabo.a24_makany.ui.screens.sheets.PlaceEditorSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreen() {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showPlaceDetailesSheet by rememberSaveable { mutableStateOf(false) }
    var showEditSheet  by rememberSaveable { mutableStateOf(false) }
    val vm: PlacesViewModel = viewModel()
    val uiState by vm.uiState.collectAsState()
    var selectedPlace by remember { mutableStateOf<PlaceEntity?>(null) }
    val filteredPlaces =
        if (searchQuery.isBlank())
            uiState.places
        else
            uiState.places.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp)
                    .padding(top = 100.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.secondary,
            )
        } else if (filteredPlaces.isEmpty()) {
            MakanySearchBar(
                searchQuery = searchQuery, onQueryChanges = { searchQuery = it })
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "No places yet, Save your first place.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

        } else {
            MakanySearchBar(
                searchQuery = searchQuery, onQueryChanges = { searchQuery = it })
            // LazyColumn
            LazyColumn(
                contentPadding = PaddingValues(bottom = 24.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredPlaces) { place ->
                    PlaceCard(
                        place = place,
                        onClick = {
                            selectedPlace = place
                            showPlaceDetailesSheet = true
                        }
                    )
                }
            }

        }
    }
    val context = LocalContext.current
    if (showPlaceDetailesSheet) {
        selectedPlace?.let { place ->
            PlaceDetailesSheet(
                place = place, onGoClick = {
                    val uri = "google.navigation:q=${place.latitude},${place.longitude}".toUri()

                    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                        setPackage("com.google.android.apps.maps")
                    }
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    }
                }, onShareClick = {
                    val shareText = """
                    📍 ${place.name}

                     ${place.address}

                       https://www.google.com/maps/search/?api=1&query=${place.latitude},${place.longitude}
                        """.trimIndent()

                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }

                    context.startActivity(
                        Intent.createChooser(intent, "Share place")
                    )
                }, onDismiss = {
                    showPlaceDetailesSheet = false
                    selectedPlace = null
                },
                onDelete = {
                    vm.deletePlace(selectedPlace!!)
                    showPlaceDetailesSheet = false
                    selectedPlace = null
                },
                onEdit = {
                    showPlaceDetailesSheet = false
                    showEditSheet = true
                }
            )

        }

    }
    if (showEditSheet) {
        selectedPlace?.let { place ->
            PlaceEditorSheet(
                place = place,
                latitude = place.latitude,
                longitude = place.longitude,
                address = place.address,
                onDismiss = {
                    showEditSheet = false
                }
            )

        }
    }
}

