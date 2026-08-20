package com.kabo.a24_makany.data.location

import android.Manifest
import android.content.Context
import android.util.Log
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

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    suspend fun fetchCurrentLocation(): LatLng? {
        Log.d("LOCATION", "fetchCurrentLocation() called")

        val location = fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .await()

        Log.d("LOCATION", "Result = $location")
        return location?.let {
            LatLng(it.latitude, it.longitude)
        }
    }

}