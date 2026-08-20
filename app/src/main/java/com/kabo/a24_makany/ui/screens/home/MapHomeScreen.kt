package com.kabo.a24_makany.ui.screens.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.checkSelfPermission
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
import android.graphics.Path
import androidx.compose.foundation.background
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.MarkerState
import com.kabo.a24_makany.ui.components.CurrentLocationCard
import com.kabo.a24_makany.ui.components.MakanyMap
import com.kabo.a24_makany.utils.LocationSettingsHandler
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MapHomeScreen(
    savePlaceRequested: Boolean,
    onReadyToOpenSheet: (LatLng, String) -> Unit
) {

    // Launcher لطلب permission
    val context = LocalContext.current
    var hasLocationPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }
    val scope = rememberCoroutineScope()

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

    /*
    // هنا بقي حاولنا نطبق DI باستخدام factory design pattern "يدوي"
    val locationHandler = LocationHandler(context)
    val geocoderHandler = GeocoderHandler(context)
    val factory = MapHomeViewModelFactory(locationHandler , geocoderHandler)
    val vm: MapHomeViewModel = viewModel(factory = factory)
    */
    val vm: MapHomeViewModel = koinViewModel()


    val userLocation by vm.userLocation.collectAsState()
    val currentAddress by vm.currentAddress.collectAsState()

    val isFetchingLocation by vm.isFetchingLocation.collectAsState()

    val locationSettingsLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                vm.fetchCurrentLocation()
            } else {
                // المستخدم رفض يشغل الـ GPS
            }
        }
    val locationSettingsHandler = remember {
        LocationSettingsHandler(context)
    }

    suspend fun checkLocationFlow() {
        if (!hasLocationPermission) return
        val request = locationSettingsHandler.checkLocationSettings()
        if (request == null) {
            vm.fetchCurrentLocation()
        } else {
            locationSettingsLauncher.launch(request)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {

                hasLocationPermission =
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    var isSavingPlace by remember { mutableStateOf(false) }
    // 1. LaunchedEffect دي وظيفتها "الطلب فقط" عند تغيير الـ permission
    LaunchedEffect(hasLocationPermission) {
        checkLocationFlow()
    }
    // 2. LaunchedEffect تانية خالص مراقبة للـ userLocation ومسؤولة عن الـ Toast
    LaunchedEffect(userLocation) {
        userLocation?.let { location ->
            vm.fetchAddress(location)
        }
    }

    LaunchedEffect(savePlaceRequested) {
        if (!savePlaceRequested) return@LaunchedEffect
        checkLocationFlow()

    }

    LaunchedEffect(userLocation, savePlaceRequested) {
        if (!savePlaceRequested || userLocation == null) return@LaunchedEffect
        isSavingPlace = true
        delay(500)
        onReadyToOpenSheet(
            userLocation!!,
            currentAddress
        )
        isSavingPlace = false
    }

    /*
    ده الجزء بتاع المانوال DI with factory
    val dao = PlacesDatabase.getInstance(context).dao
    val repo = PlacesRepository(dao)
    val placesFactory = PlacesViewModelFactory(repo)
    val placesVM: PlacesViewModel = viewModel(factory = placesFactory)
    val uiState by placesVM.uiState.collectAsState()
    */
    val placesVM : PlacesViewModel = koinViewModel()
    val uiState by placesVM.uiState.collectAsState()


    Box(modifier = Modifier.fillMaxSize()) {

        MakanyMap(
            modifier = Modifier.fillMaxSize(),
            userLocation = userLocation,
            isSavingPlace = isSavingPlace,
            places = uiState.places
        )
        CurrentLocationCard(
            address = currentAddress,
            hasLocationPermission = hasLocationPermission,
            isFetchingLocation = isFetchingLocation,
            userLocation = userLocation,
            onRefresh = {
                scope.launch {
                    checkLocationFlow()
                }
            }
        )

    }
}
