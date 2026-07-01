package com.kabo.a24_makany.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddHome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material.icons.outlined.AddHome
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kabo.a24_makany.R
import com.kabo.a24_makany.data.local.PlaceEntity
import com.kabo.a24_makany.ui.theme.Primary
import com.kabo.a24_makany.ui.theme.Secondary
import com.kabo.a24_makany.ui.theme.Shape
import com.kabo.a24_makany.ui.theme.Surface
import java.io.File


@Composable
fun PlaceCard(
    place: PlaceEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = Shape.medium,
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // الصورة
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(Shape.medium)
                    .background(Secondary)
            ) {
                if (place.imageUri != null) {
                    AsyncImage(
                        model = File(place.imageUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.icon),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(22.dp)
                    )
                }
            }
            // النص
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = place.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = Ellipsis
                )
                if (place.notes.isNotBlank()) {
                    Text(
                        text = place.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = when (place.category) {
                            "Home" -> Icons.Filled.Home
                            "Work" -> Icons.Filled.WorkOutline
                            "Food" -> Icons.Filled.Restaurant
                            "Cafe" -> Icons.Filled.LocalCafe
                            "Other" -> Icons.Filled.AddHome
                            else -> Icons.Filled.Park
                        },
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = " ${place.category}",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = place.address,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = Ellipsis
                )
            }

            // السهم
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Secondary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = Primary
                )
            }
        }
    }
}