package com.kabo.a24_makany.ui.screens.sheets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.AddHome
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Directions
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.ShareLocation
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kabo.a24_makany.R
import com.kabo.a24_makany.data.local.PlaceEntity
import com.kabo.a24_makany.ui.components.SheetsButton
import com.kabo.a24_makany.ui.theme.Primary
import com.kabo.a24_makany.ui.theme.Secondary
import com.kabo.a24_makany.ui.theme.Shape
import com.kabo.a24_makany.ui.theme.Surface
import java.io.File


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PlaceDetailesSheet(
    place: PlaceEntity,
    onGoClick: () -> Unit,
    onShareClick: () -> Unit,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            //place name

                Text(
                    place.name,
                    style = MaterialTheme.typography.titleLarge,
                )
            Spacer(modifier = Modifier.height(8.dp))
            //photo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .clip(Shape.medium)
                        .background(Secondary),
                    contentAlignment = Alignment.Center
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
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Edit
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .weight(1f)
                            .clip(Shape.medium)
                            .background(Surface)
                            .clickable {
                                // TODO Edit
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit",
                            tint = Primary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Delete
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .weight(1f)
                            .clip(Shape.medium)
                            .background(Surface)
                            .clickable {
                                // TODO Delete
                                onDelete()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = "Delete",
                            tint = Primary
                        )
                    }
                }
            }
            //note + category
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Note",
                style = MaterialTheme.typography.titleSmall
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)

            ) {
                Box(
                    modifier = Modifier
                        .clip(Shape.small)
                        .background(Surface)
                        .padding(8.dp)
                        .weight(1f),
                ) {
                    Text(
                        text = place.notes,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                // Category
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = 6.dp)
                        .clip(Shape.small)
                        .background(Color(0xFFE5E5E5)),

                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            imageVector = when (place.category) {
                                "Home" -> Icons.Outlined.Home
                                "Work" -> Icons.Outlined.WorkOutline
                                "Food" -> Icons.Outlined.Restaurant
                                "Cafe" -> Icons.Outlined.LocalCafe
                                "Other" -> Icons.Outlined.AddHome
                                else -> Icons.Outlined.Park
                            },
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = place.category,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }


            }
            //address
            Text(
                "Address",
                style = MaterialTheme.typography.titleSmall,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(Shape.small)
                    .background(Color(0xFFE5E5E5))
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Map,
                        contentDescription = null,
                        Modifier
                            .size(18.dp)
                            .padding(end = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        place.address,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            //buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
            ) {
                SheetsButton(
                    "Go",
                    Icons.Outlined.Directions,
                    modifier = Modifier.weight(1f),
                    onClick = onGoClick
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(Shape.medium)
                        .background(Surface)
                        .clickable { onShareClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ShareLocation,
                        contentDescription = null,
                        tint = Primary
                    )
                }
            }
        }
    }
}