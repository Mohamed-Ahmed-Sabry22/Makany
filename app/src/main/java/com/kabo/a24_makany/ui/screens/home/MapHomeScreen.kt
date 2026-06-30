package com.kabo.a24_makany.ui.screens.home

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Camera
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.kabo.a24_makany.ui.components.MakanySearchBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

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
            Toast.makeText(
                context,
                "your location ${location.latitude}, ${location.longitude}.",
                Toast.LENGTH_SHORT
            ).show()
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
        delay(800)
        onReadyToOpenSheet(
            userLocation!!,
            currentAddress
        )

    }




    Box(modifier = Modifier.fillMaxSize()) {
        MakanyMap(
            modifier = Modifier.fillMaxSize(),
            userLocation = userLocation,
            isSavingPlace = isSavingPlace
        )
        MakanySearchBar(
            searchQuery = searchQuery,
            onQueryChanges = { it ->
                searchQuery = it
                //المفرةض هنا بيحصل حاجه بيجيب المكان ممكن
            }
        )
    }
}


@SuppressLint("UnrememberedMutableState")
@Composable
fun MakanyMap(modifier: Modifier = Modifier, userLocation: LatLng?, isSavingPlace: Boolean) {
    val defaultLocation = LatLng(30.0444, 31.2357)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 15f)
    }
    LaunchedEffect(userLocation, isSavingPlace) {
        userLocation?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    it,
                    if (isSavingPlace) 19f else 17f
                ),
                durationMs = 800
            )

        }
    }
    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapType = MapType.NORMAL
        ),
    ) {
        userLocation?.let {
            Marker(
                state = rememberMarkerState(position = userLocation),
                title = "You are here",
                icon = BitmapDescriptorFactory.defaultMarker(
                    if (isSavingPlace) {
                        180f // 🟢 رقم بيدي درجة أخضر شيك (جربها هتعجبك)
                    } else {
                        240f // 🔵 رقم بيدي درجة أزرق ميريال هادية (مش الفاقع الافتراضي)
                    }
                )
            )
        }


    }
}