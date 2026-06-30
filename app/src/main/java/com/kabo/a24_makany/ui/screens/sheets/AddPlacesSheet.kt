package com.kabo.a24_makany.ui.screens.sheets

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.AddHome
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kabo.a24_makany.model.Place
import com.kabo.a24_makany.ui.components.SheetTextField
import com.kabo.a24_makany.ui.components.SheetsButton
import com.kabo.a24_makany.ui.theme.Primary
import com.kabo.a24_makany.ui.theme.Shape
import com.kabo.a24_makany.ui.theme.Surface


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddPlaceSheet(
    latitude: Double?,
    longitude: Double?,
    address: String,
    onDismiss: () -> Unit,
) {

    val categories = listOf("Home", "Work", "Food", "Cafe", "Park", "Other")
    var isNameError by remember { mutableStateOf(false) }
    var placeName by remember { mutableStateOf("") }
    var placeNotes by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Home") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
    }
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)

        ) {
            Text(
                "Add Place",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(Shape.medium)
                    .background(Color.LightGray)
                    .clickable { imagePicker.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AddAPhoto,
                            contentDescription = null
                        )
                        Text(
                            "Add Place",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
            SheetTextField(
                value = placeName,
                onValueChange = {
                    placeName = it
                    if (isNameError) isNameError = false
                },
                labelText = "Place Name",
                placeHolderText = "e.g.Home, Work, Favorite Cafe",
                isError = isNameError,
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 6.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selectedCategory == category,
                            borderColor = Color.Transparent,
                            selectedBorderColor = Color.Transparent
                        ),
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = when (category) {
                                        "Home" -> Icons.Outlined.Home
                                        "Work" -> Icons.Outlined.WorkOutline
                                        "Food" -> Icons.Outlined.Restaurant
                                        "Cafe" -> Icons.Outlined.LocalCafe
                                        "Other" -> Icons.Outlined.AddHome
                                        else -> Icons.Outlined.Park
                                    },
                                    contentDescription = null,
                                    Modifier.size(16.dp),
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    category,
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Primary,
                            selectedLabelColor = Color.White,
                            containerColor = Surface,
                            labelColor = Color.Gray
                        )
                    )
                }
            }
            SheetTextField(
                value = placeNotes,
                onValueChange = { placeNotes = it },
                lines = 5,
                labelText = "Notes",
                placeHolderText = "Add some notes about this place...",
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Current Address",
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
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Map,
                        contentDescription = null,
                        Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(address)
                }
            }
            val context = LocalContext.current
            Row(
                modifier = Modifier.padding(vertical = 12.dp),
            ) {
                SheetsButton(
                    "Save Place",
                    Icons.Rounded.BookmarkBorder,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (placeName.isBlank()) {
                            isNameError = true
                            return@SheetsButton
                        }
                        // الكود سليم.. كمل حفظ المكان
                        isNameError = false
                        val place = Place(
                            name = placeName.trim(),
                            notes = placeNotes,
                            category = selectedCategory,
                            latitude = latitude,
                            longitude = longitude,
                            address = address,
                            imageUri = selectedImageUri?.toString()
                        )
                        Toast.makeText(context, "place saved", Toast.LENGTH_SHORT).show()
                        Log.d("PLACE", place.toString())
                        onDismiss()
                        // vm.savePlace(placeName)


                    }
                )
            }

        }

    }
}
