package com.kabo.a24_makany

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kabo.a24_makany.screens.NavGraph
import com.kabo.a24_makany.ui.theme.Accent
import com.kabo.a24_makany.ui.theme.Primary
import com.kabo.a24_makany.ui.theme.Secondary
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
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (showTopBar) TopBar()
                    },
                    bottomBar = {
                        if (showBottomBar) BottomNavigationBar(currentDestination, navController)
                    },
                    floatingActionButton = {
                        if (showFAB) FAB()
                    }
                ) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
private fun FAB() {
    FloatingActionButton(
        onClick = {},
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

