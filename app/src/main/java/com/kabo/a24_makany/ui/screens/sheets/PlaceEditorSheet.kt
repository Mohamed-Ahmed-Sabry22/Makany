package com.kabo.a24_makany.ui.screens.sheets

import android.content.pm.PackageManager
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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.outlined.PhotoLibrary
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.kabo.a24_makany.data.local.PlaceEntity
import com.kabo.a24_makany.ui.components.SheetTextField
import com.kabo.a24_makany.ui.components.SheetsButton
import com.kabo.a24_makany.ui.screens.places.PlacesViewModel
import com.kabo.a24_makany.ui.theme.Primary
import com.kabo.a24_makany.ui.theme.Shape
import com.kabo.a24_makany.ui.theme.Surface
import com.kabo.a24_makany.utils.ImageStorageHelper


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PlaceEditorSheet(
    place: PlaceEntity? = null,
    latitude: Double?,
    longitude: Double?,
    address: String,
    onDismiss: () -> Unit,
) {

    val categories = listOf("Home", "Work", "Food", "Cafe", "Park", "Other")
    var isNameError by remember { mutableStateOf(false) }

    var placeName by remember(place) {
        mutableStateOf(place?.name ?: "")
    }

    var placeNotes by remember(place) {
        mutableStateOf(place?.notes ?: "")
    }

    var selectedCategory by remember(place) {
        mutableStateOf(place?.category ?: "Home")
    }

    var currentImagePath by remember(place) {
        mutableStateOf(place?.imageUri)
    }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
    }
    val context = LocalContext.current

    val cameraImageUri = remember {
        mutableStateOf<Uri?>(null)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            selectedImageUri = cameraImageUri.value
        }
    }
    var pendingCamera by remember { mutableStateOf(false) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->

        if (granted && pendingCamera) {
            pendingCamera = false

            val helper = ImageStorageHelper(context)
            val uri = helper.createCameraImageUri()

            cameraImageUri.value = uri
            cameraLauncher.launch(uri)
        }
    }

    val vm: PlacesViewModel = viewModel()
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)

        ) {
            Text(
                if (place == null) "Add Place" else "Edit PLace",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .weight(1f)
                        .clip(Shape.medium)
                        .background(Color.LightGray)
                        .clickable {},
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        selectedImageUri != null -> {
                            // الصورة الجديدة من الكاميرا أو الجاليري
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        currentImagePath != null -> {
                            // الصورة القديمة من Room
                            AsyncImage(
                                model = currentImagePath,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        else -> {
                            // Placeholder
                            Text(
                                "Put Your Photo here",
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Box(
                        modifier = Modifier
                            .width(70.dp)
                            .weight(0.5f)
                            .clip(Shape.medium)
                            .background(Surface)
                            .clickable {

                                if (ContextCompat.checkSelfPermission(
                                        context, android.Manifest.permission.CAMERA
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {

                                    val helper = ImageStorageHelper(context)
                                    val uri = helper.createCameraImageUri()

                                    cameraImageUri.value = uri
                                    cameraLauncher.launch(uri)

                                } else {

                                    pendingCamera = true
                                    cameraPermissionLauncher.launch(
                                        android.Manifest.permission.CAMERA
                                    )

                                }

                            }, contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AddAPhoto,
                            contentDescription = null,
                            tint = Primary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .width(70.dp)
                            .weight(0.5f)
                            .clip(Shape.medium)
                            .background(Surface)
                            .clickable {
                                imagePicker.launch("image/*")
                            }, contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PhotoLibrary,
                            contentDescription = null,
                            tint = Primary
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
                    verticalAlignment = Alignment.Top, modifier = Modifier.padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Map,
                        contentDescription = null,
                        Modifier
                            .size(18.dp)
                            .padding(end = 4.dp)
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
                    if (place == null) "Save Place" else "Update PLace",
                    Icons.Rounded.BookmarkBorder,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (place == null) {
                            if (placeName.isBlank()) {
                                isNameError = true
                                return@SheetsButton
                            }
                            // الكود سليم.. كمل حفظ المكان
                            isNameError = false
                            val imageHelper = ImageStorageHelper(context)
                            val savedImagePath = selectedImageUri?.let {
                                imageHelper.saveImage(it)
                            }
                            val place = PlaceEntity(
                                name = placeName.trim(),
                                notes = placeNotes,
                                category = selectedCategory,
                                latitude = latitude,
                                longitude = longitude,
                                address = address,
                                imageUri = savedImagePath
                            )
                            vm.savePlace(place)
                            onDismiss()
                        } else {

                            if (placeName.isBlank()) {
                                isNameError = true
                                return@SheetsButton
                            }

                            isNameError = false

                            val imageHelper = ImageStorageHelper(context)

                            val savedImagePath =
                                if (selectedImageUri != null) {
                                    imageHelper.saveImage(selectedImageUri!!)
                                } else {
                                    currentImagePath
                                }

                            val updatedPlace = place.copy(
                                name = placeName.trim(),
                                notes = placeNotes,
                                category = selectedCategory,
                                imageUri = savedImagePath
                            )
                            vm.savePlace(updatedPlace)

                            onDismiss()
                        }

                    })
            }

        }

    }
}
