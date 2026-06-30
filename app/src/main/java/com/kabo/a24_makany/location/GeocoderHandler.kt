package com.kabo.a24_makany.location

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

class GeocoderHandler(
    context: Context
) {

    private val geocoder = Geocoder(context, Locale.getDefault())

    suspend fun getAddress(latLng: LatLng): String {

        return try {

            val addresses = geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            )

            if (!addresses.isNullOrEmpty()) {
                addresses[0].getAddressLine(0)
            } else {
                "Unknown address"
            }

        } catch (e: Exception) {
            e.printStackTrace()
            "Unknown address"
        }
    }
}