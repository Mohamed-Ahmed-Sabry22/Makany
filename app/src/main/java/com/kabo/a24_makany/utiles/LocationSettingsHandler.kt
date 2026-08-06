package com.kabo.a24_makany.utils

import android.content.Context
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

class LocationSettingsHandler(
    context: Context
) {

    private val settingsClient =
        LocationServices.getSettingsClient(context)

    suspend fun checkLocationSettings(): IntentSenderRequest? {

        val locationRequest =
            LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                1000L
            ).build()

        val settingsRequest =
            LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build()

        return try {

            settingsClient
                .checkLocationSettings(settingsRequest)
                .await()
            null

        } catch (e: ResolvableApiException) {

            IntentSenderRequest.Builder(e.resolution)
                .build()

        }
        catch (e: Exception) {

            null
        }
    }
}