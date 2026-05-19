package com.kabo.a24_makany

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.kabo.a24_makany.ui.navigation.NavGraph
import com.kabo.a24_makany.ui.theme.Accent
import com.kabo.a24_makany.ui.theme.Primary
import com.kabo.a24_makany.ui.theme.Shape
import com.kabo.a24_makany.ui.theme.Surface
import com.kabo.a24_makany.ui.theme._24_MakanyTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _24_MakanyTheme(
                darkTheme = false,
            ) {
                val navController = rememberNavController()
                val currentDestination =
                    navController.currentBackStackEntryAsState().value?.destination?.route
                val showBottomBar = currentDestination != "login" && currentDestination != "signup"
                val showTopBar = currentDestination == "places"
                val showFAB = currentDestination == "home"
                var showAddPlaceSheet by remember { mutableStateOf(false) }

                var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
                val imagePicker = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent()
                ) { uri ->
                    selectedImageUri = uri
                }
                var placeName by remember { mutableStateOf("") }
                var placeNotes by remember { mutableStateOf("") }
                val categories = listOf("Home" , "Work" , "Food" , "Cafe" , "Park")
                var selectedCategory by remember { mutableStateOf("Home") }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (showTopBar) TopBar()
                    },
                    bottomBar = {
                        if (showBottomBar) BottomNavigationBar(currentDestination, navController)
                    },
                    floatingActionButton = {
                        if (showFAB) FAB(onClick = { showAddPlaceSheet = true })
                    }
                ) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                    if (showAddPlaceSheet) {
                        showAddPlaceSheet = AddPlaceSheet(
                            showAddPlaceSheet,
                            imagePicker,
                            selectedImageUri,
                            placeName,
                            categories,
                            selectedCategory,
                            placeNotes
                        )
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AddPlaceSheet(
    showAddPlaceSheet: Boolean,
    imagePicker: ManagedActivityResultLauncher<String, Uri?>,
    selectedImageUri: Uri?,
    placeName: String,
    categories: List<String>,
    selectedCategory: String,
    placeNotes: String
): Boolean {
    var showAddPlaceSheet1 = showAddPlaceSheet
    var placeName1 = placeName
    var selectedCategory1 = selectedCategory
    var placeNotes1 = placeNotes
    ModalBottomSheet(
        onDismissRequest = { showAddPlaceSheet1 = false }
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
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                value = placeName1,
                onValueChange = { placeName1 = it },
                label = {
                    Text(
                        "Place Name",
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                placeholder = {
                    Text(
                        "e.g.Home, Work, Favorite Cafe",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF7C7C7C)
                    )
                },
                shape = Shape.small
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 6.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selectedCategory1 == category,
                            borderColor = Color.Transparent,
                            selectedBorderColor = Color.Transparent
                        ),
                        selected = selectedCategory1 == category,
                        onClick = { selectedCategory1 = category },
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
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                value = placeNotes1,
                onValueChange = { placeNotes1 = it },
                minLines = 5,
                label = {
                    Text(
                        "Notes",
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                placeholder = {
                    Text(
                        "Add some notes about this place...",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF7C7C7C)
                    )
                },
                shape = Shape.small
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
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("12 Elshopan - sohag")
                }
            }
            ElevatedButton(
                onClick = {},
                shape = Shape.medium,
                colors = ButtonDefaults.elevatedButtonColors(Primary),
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.BookmarkBorder,
                        contentDescription = null,
                        Modifier.size(16.dp),
                        tint = Surface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Save Place",
                        style = MaterialTheme.typography.titleSmall,
                        color = Surface
                    )
                }
            }
        }

    }
    return showAddPlaceSheet1
}

@Composable
private fun FAB(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = {
            onClick()
        },
        containerColor = Accent,
        shape = Shape.large
    ) {
        Icon(
            imageVector = Icons.Outlined.Place,
            contentDescription = null,
            tint = Color.White,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_outlined),
                    contentDescription = "Makany Logo",
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "Makany",
                    style = MaterialTheme.typography.titleLarge,
                    color = Primary
                )
            }
        }
    )
}

@Composable
private fun BottomNavigationBar(
    currentDestination: String?,
    navController: NavHostController
) {
    NavigationBar(
        containerColor = Surface,
    ) {
        BottomNavigationBarItem(
            "home",
            "Map",
            currentDestination,
            navController,
            if (currentDestination == "home") Icons.Filled.Map
            else Icons.Outlined.Map
        )
        BottomNavigationBarItem(
            "places",
            "Places",
            currentDestination,
            navController,
            if (currentDestination == "places") Icons.AutoMirrored.Filled.FormatListBulleted
            else Icons.AutoMirrored.Outlined.FormatListBulleted
        )
    }

}

@Composable
private fun RowScope.BottomNavigationBarItem(
    route: String,
    labelText: String,
    currentDestination: String?,
    navController: NavHostController,
    icon: ImageVector
) {
    NavigationBarItem(
        selected = currentDestination == route,
        onClick = {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
            }
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        label = {
            Text(labelText, style = MaterialTheme.typography.labelLarge)
        },
        alwaysShowLabel = true,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Primary,
            selectedTextColor = Primary,
            unselectedIconColor = Color.Gray,
            unselectedTextColor = Color.Gray,
            indicatorColor = Color.Gray.copy(alpha = 0.1f)
        )
    )
}

