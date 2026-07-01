package com.kabo.a24_makany.ui.screens.places

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreen() {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showPlaceDetailesSheet by rememberSaveable { mutableStateOf(false) }
    val vm: PlacesViewModel = viewModel()
    val places by vm.places.collectAsState(initial = emptyList())
    var selectedPlace by remember { mutableStateOf<PlaceEntity?>(null) }
    val filteredPlaces =
        if (searchQuery.isBlank())
            places
        else
            places.filter {
                it.name.contains(searchQuery, true)
            }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MakanySearchBar(
            searchQuery = searchQuery, onQueryChanges = { searchQuery = it })
        // 1. التشييك على اللستة لو فاضية
        if (filteredPlaces.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // بياخد باقي مساحة الشاشة تحت السيرش بار
                contentAlignment = Alignment.Center // بيخلي العناصر في السنتر بالظبط
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // أيقونة توحي بالبحث أو الفضاء
                    Icon(
                        imageVector = Icons.Default.SearchOff, // أو Icons.Default.Inbox
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
            // 2. إيلس: لو اللستة فيها بيانات يعرض كودك القديم زي ما هو
            LazyColumn(
                contentPadding = PaddingValues(bottom = 24.dp),
                modifier = Modifier.weight(1f) // عشان السكرول يظبط مع السيرش بار
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
                Toast.makeText(context, "gone done", Toast.LENGTH_SHORT).show()
                val uri = "google.navigation:q=${place.latitude},${place.longitude}".toUri()

                val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                    setPackage("com.google.android.apps.maps")
                }
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
            },
                onDelete = {
                    vm.deletePlace(selectedPlace!!)
                    showPlaceDetailesSheet = false
                    selectedPlace = null
                }
            )

        }

    }
}

