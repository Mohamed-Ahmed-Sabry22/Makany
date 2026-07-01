package com.kabo.a24_makany.ui.screens.places

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kabo.a24_makany.data.local.PlaceEntity
import com.kabo.a24_makany.ui.components.MakanySearchBar
import com.kabo.a24_makany.ui.components.PlaceCard
import com.kabo.a24_makany.ui.screens.sheets.PlaceDetailesSheet
import androidx.core.net.toUri
import com.google.android.libraries.places.api.model.kotlin.place

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreen() {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showPlaceDetailesSheet by rememberSaveable { mutableStateOf(false) }
    val vm: PlacesViewModel = viewModel()
    val places by vm.places.collectAsState(initial = emptyList())
    var selectedPlace by remember { mutableStateOf<PlaceEntity?>(null) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MakanySearchBar(
            searchQuery = searchQuery, onQueryChanges = { searchQuery = it })
        LazyColumn() {
            items(places) { place ->
                PlaceCard(
                    place, onClick = {
                        selectedPlace = place
                        showPlaceDetailesSheet = true
                    })
            }
        }
    }
    val context = LocalContext.current
    if (showPlaceDetailesSheet) {
        selectedPlace?.let { place ->
            PlaceDetailesSheet(place = place, onGoClick = {
                Toast.makeText(context, "gone done", Toast.LENGTH_SHORT).show()
                val uri = "google.navigation:q=${place.latitude},${place.longitude}".toUri()

                val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                    setPackage("com.google.android.apps.maps")
                }

                context.startActivity(intent)

                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            }, onShareClick = {
                Toast.makeText(context, "place shared", Toast.LENGTH_SHORT).show()
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
            })
        }

    }
}

