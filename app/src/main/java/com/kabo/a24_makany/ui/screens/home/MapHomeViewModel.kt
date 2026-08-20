package com.kabo.a24_makany.ui.screens.home

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.kabo.a24_makany.data.location.GeocoderHandler
import com.kabo.a24_makany.data.location.LocationHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapHomeViewModel(
    private val locationHandler : LocationHandler,
    private val geocoderHandler : GeocoderHandler
) : ViewModel() {

    //private val locationHandler = LocationHandler(getApplication())
    //private val geocoderHandler = GeocoderHandler(getApplication())

    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation = _userLocation.asStateFlow()
    val isFetchingLocation = MutableStateFlow(false)
    private val _currentAddress = MutableStateFlow("")
    val currentAddress = _currentAddress.asStateFlow()

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun fetchCurrentLocation() {
        viewModelScope.launch {
            isFetchingLocation.value = true
            _userLocation.value =
                locationHandler.fetchCurrentLocation()
            isFetchingLocation.value = false
        }
    }

    fun fetchAddress(location: LatLng) {
        viewModelScope.launch {

            _currentAddress.value =
                geocoderHandler.getAddress(location)

        }

    }

}