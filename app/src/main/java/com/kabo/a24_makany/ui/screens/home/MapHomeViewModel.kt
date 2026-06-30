package com.kabo.a24_makany.ui.screens.home

import android.Manifest
import android.app.Application
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.kabo.a24_makany.data.local.PlaceEntity
import com.kabo.a24_makany.data.local.PlacesDatabase
import com.kabo.a24_makany.data.repository.PlacesRepository
import com.kabo.a24_makany.location.GeocoderHandler
import com.kabo.a24_makany.location.LocationHandler
import com.kabo.a24_makany.model.Place
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapHomeViewModel(application : Application) : AndroidViewModel(application) {

    private val locationHandler = LocationHandler(getApplication())

    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation = _userLocation.asStateFlow()


    private val geocoderHandler =
        GeocoderHandler(getApplication())
    private val _currentAddress = MutableStateFlow("")
    val currentAddress = _currentAddress.asStateFlow()

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun fetchCurrentLocation() {
        viewModelScope.launch {
            _userLocation.value =
                locationHandler.fetchCurrentLocation()
        }
    }

    fun fetchAddress(location: LatLng) {

        viewModelScope.launch {

            _currentAddress.value =
                geocoderHandler.getAddress(location)

        }

    }

}