package com.kabo.a24_makany

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kabo.a24_makany.screens.NavGraph
import com.kabo.a24_makany.ui.theme.Primary
import com.kabo.a24_makany.ui.theme.Surface
import com.kabo.a24_makany.ui.theme._24_MakanyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            _24_MakanyTheme(
                darkTheme = false,

                ) {
                val navController = rememberNavController()
                val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(currentDestination, navController)
                    }
                ) { innerPadding ->
                    NavGraph(navController = navController , modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(
    currentDestination: String?,
    navController: NavHostController
) {
    NavigationBar(
        containerColor = Surface,
    ) {
        BottomNavigationBarItem("home","Map",currentDestination, navController ,
            if (currentDestination == "home") Icons.Filled.Map else Icons.Outlined.Map)
        BottomNavigationBarItem("places","Places",currentDestination, navController ,
            if (currentDestination == "places") Icons.AutoMirrored.Filled.FormatListBulleted else Icons.AutoMirrored.Outlined.FormatListBulleted,)

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
        onClick = { navController.navigate(route){
            launchSingleTop = true
            restoreState = true
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
        } },
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

