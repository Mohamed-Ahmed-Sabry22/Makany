package com.kabo.a24_makany.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.kabo.a24_makany.ui.theme.Primary
import com.kabo.a24_makany.ui.theme.Surface

@Composable
fun MapHomeScreen() {
    var searchQuery by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        MakanyMap(modifier = Modifier.fillMaxSize())
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.TopCenter),
            placeholder = { Text("Search saved places...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null
                )
            },
            shape = RoundedCornerShape(50.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Surface,
                focusedContainerColor = Surface,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Primary
            ),
            singleLine = true
        )
    }
}
@Composable
fun MakanyMap(modifier: Modifier = Modifier) {
    val cairo = LatLng(30.0444, 31.2357)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cairo,18f) }
    val cairoMarkerState = remember { MarkerState(cairo) }
    var clickedLocation by remember { mutableStateOf<LatLng?>(null) }


    LaunchedEffect(clickedLocation) {
        clickedLocation?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(it,14f),
                durationMs = 700
            )
        }
    }
    GoogleMap (
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapType = MapType.NORMAL,
            maxZoomPreference = 20f,
            minZoomPreference = 5f,
        ),
        onMapClick = {
            latlng->
            clickedLocation = latlng
        }
    ){
        Marker(
            state = cairoMarkerState
        )
        clickedLocation?.let {
            Marker(state = MarkerState(it))
        }
    }
}