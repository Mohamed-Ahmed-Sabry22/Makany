package com.kabo.a24_makany.data.location

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

            val address = geocoder.getFromLocation(
                latLng.latitude, latLng.longitude, 1
            )?.firstOrNull()

            if (address == null) return "Unknown address"

            buildList {

                address.featureName?.takeIf {
                        it.isNotBlank() && !it.contains("+") && !it.equals(
                            "Unnamed Road",
                            true
                        ) && !it.all { c -> c.isDigit() }
                    }?.let { add(it) }

                address.thoroughfare?.takeIf { it.isNotBlank() }?.let { add(it) }

                address.subLocality?.takeIf { it.isNotBlank() }?.let { add(it) }

                address.locality?.takeIf { it.isNotBlank() }?.let { add(it) }

                address.subAdminArea
                    ?.takeIf { it.isNotBlank() }
                    ?.let { add(it) }

            }.distinct().joinToString(", ").ifBlank { "Unknown address" }

        } catch (e: Exception) {
            e.printStackTrace()
            "Unknown address"
        }
    }
}