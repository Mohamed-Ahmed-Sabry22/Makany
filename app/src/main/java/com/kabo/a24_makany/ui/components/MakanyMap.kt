package com.kabo.a24_makany.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.MarkerState.Companion.invoke
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.kabo.a24_makany.R
import com.kabo.a24_makany.data.local.PlaceEntity
import com.kabo.a24_makany.utils.bitmapDescriptorFromColor
import kotlin.collections.forEach

@SuppressLint("UnrememberedMutableState")
@Composable
fun MakanyMap(
    modifier: Modifier = Modifier,
    userLocation: LatLng?,
    isSavingPlace: Boolean,
    places: List<PlaceEntity>
) {
    var isMapLoaded by remember { mutableStateOf(false) }
    val defaultLocation = LatLng(30.0444, 31.2357)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 15f)
    }
    LaunchedEffect(userLocation, isSavingPlace) {
        userLocation?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    it,
                    if (isSavingPlace) 18f else 17f
                ),
                durationMs = 800
            )

        }
    }
    val context = LocalContext.current
    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapType = MapType.NORMAL,
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                context,
                R.raw.map_style
            )
        ),
        onMapLoaded = {
            isMapLoaded = true
        }
    ) {
        userLocation?.let {
            Marker(
                state = rememberMarkerState(position = userLocation),
                title = "You are here",
                icon = bitmapDescriptorFromColor(
                    if (isSavingPlace) Color(0xFF2D6A4F)// Primary
                    else Color(0xFFFF9800) // Accent
                )
            )
        }
        places.forEach { place ->
            val lat = place.latitude ?: return@forEach
            val lng = place.longitude ?: return@forEach

            Marker(
                state = MarkerState(LatLng(lat, lng)),
                title = place.name,
                icon = bitmapDescriptorFromColor(Color(0xFF2D6A4F)), // Primary
                snippet = place.category

            )
        }


    }
    if (!isMapLoaded) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}