package com.kabo.a24_makany.location

import android.content.Context
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.tasks.await

class LocationHandler(
    private val context: Context
) {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    @RequiresPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    suspend fun fetchCurrentLocation(): LatLng? {
        val location = fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .await()
        return location?.let {
            LatLng(it.latitude, it.longitude)
        }
    }

}