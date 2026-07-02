package com.kabo.a24_makany.ui.screens.home

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.kabo.a24_makany.R
import com.kabo.a24_makany.data.local.PlaceEntity
import com.kabo.a24_makany.ui.screens.places.PlacesViewModel
import com.kabo.a24_makany.ui.theme.Secondary
import kotlinx.coroutines.delay
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.ui.graphics.toArgb
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
@Composable
fun MapHomeScreen(
    savePlaceRequested: Boolean,
    onReadyToOpenSheet: (LatLng, String) -> Unit
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }

    // Launcher لطلب permission
    val context = LocalContext.current
    var hasLocationPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }
    // أول ما الشاشة تفتح → تحقق من permission
    LaunchedEffect(Unit) {
        hasLocationPermission = checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        // لو مش موجود → اطلبه
        if (!hasLocationPermission) {
            permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }

    }


    val vm: MapHomeViewModel = viewModel()
    val userLocation by vm.userLocation.collectAsState()
    val currentAddress by vm.currentAddress.collectAsState()

    var isSavingPlace by remember { mutableStateOf(false) }
    // 1. LaunchedEffect دي وظيفتها "الطلب فقط" عند تغيير الـ permission
    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            vm.fetchCurrentLocation()
        }
    }
    // 2. LaunchedEffect تانية خالص مراقبة للـ userLocation ومسؤولة عن الـ Toast
    LaunchedEffect(userLocation) {
        userLocation?.let { location ->
            vm.fetchAddress(location)
        }
    }

    LaunchedEffect(savePlaceRequested) {
        if (!savePlaceRequested) return@LaunchedEffect
        // المرحلة الأولى
        vm.fetchCurrentLocation()

    }

    LaunchedEffect(userLocation, savePlaceRequested) {
        if (!savePlaceRequested || userLocation == null) return@LaunchedEffect
        isSavingPlace = true
        delay(500)
        onReadyToOpenSheet(
            userLocation!!,
            currentAddress
        )

    }

    val placesvm: PlacesViewModel = viewModel()
    val places by placesvm.places.collectAsState(initial = emptyList())


    Box(modifier = Modifier.fillMaxSize()) {

        MakanyMap(
            modifier = Modifier.fillMaxSize(),
            userLocation = userLocation,
            isSavingPlace = isSavingPlace,
            places = places
        )
        CurrentLocationCard(
            address = currentAddress,
            onRefresh = {
                vm.fetchCurrentLocation()
            }
        )

    }
}


@SuppressLint("UnrememberedMutableState")
@Composable
fun MakanyMap(
    modifier: Modifier = Modifier,
    userLocation: LatLng?,
    isSavingPlace: Boolean,
    places: List<PlaceEntity>
) {
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
        )
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
                state = rememberMarkerState(
                    position = LatLng(lat, lng)
                ),
                title = place.name,
                icon = bitmapDescriptorFromColor(Color(0xFF2D6A4F)), // Primary
                snippet = place.category

            )
        }


    }
}

@Composable
fun CurrentLocationCard(
    address: String,
    onRefresh: () -> Unit
) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.elevatedCardColors(Secondary)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Outlined.MyLocation,
                contentDescription = null,
                tint = Color.Black
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    "Current Location",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black
                )

                Text(
                    if (address.isBlank()) "Getting location..."
                    else address,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    color = Color.Black
                )
            }

            IconButton(
                onClick = onRefresh
            ) {
                Icon(
                    Icons.Outlined.Refresh,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
    }
}
fun bitmapDescriptorFromColor(color: Color): BitmapDescriptor {
    val colorInt = color.toArgb()

    val paint = android.graphics.Paint().apply {
        this.color = colorInt
        isAntiAlias = true
    }

    val bitmap = Bitmap.createBitmap(90, 120, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)

// الدائرة أكبر
    canvas.drawCircle(30f, 30f, 30f, paint)

// الذيل أكبر
    val path = Path().apply {
        moveTo(18f, 52f)
        lineTo(42f, 52f)
        lineTo(30f, 82f)
        close()
    }
    canvas.drawPath(path, paint)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}
