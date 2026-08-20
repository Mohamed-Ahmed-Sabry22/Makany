package com.kabo.a24_makany.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.kabo.a24_makany.ui.theme.Secondary

@Composable
fun CurrentLocationCard(
    address: String,
    userLocation: LatLng?,
    hasLocationPermission : Boolean,
    isFetchingLocation : Boolean,
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
                    when {
                        !hasLocationPermission -> "Location permission denied"
                        isFetchingLocation -> "Getting location..."
                        address.isBlank() && userLocation == null -> "Please turn on your location"
                        address.isBlank() -> "Getting address..."
                        else -> address
                    },
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