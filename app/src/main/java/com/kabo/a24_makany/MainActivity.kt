package com.kabo.a24_makany

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
                        NavigationBar(
                            containerColor = Surface,
                        ) {
                            NavigationBarItem(
                                selected = currentDestination == "home",
                                onClick = {navController.navigate("home")},
                                icon = {Icon(
                                        imageVector = if(currentDestination == "home") Icons.Filled.Map else Icons.Outlined.Map,
                                        contentDescription = null
                                    )},
                                label ={
                                    Text("Map",style = MaterialTheme.typography.titleSmall)
                                },
                                alwaysShowLabel = true,
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Primary,
                                    selectedTextColor = Primary,
                                    unselectedIconColor = Color.Gray,
                                    unselectedTextColor = Color.Gray,
                                    indicatorColor = Color.Gray.copy(alpha = 0.1f))
                            )
                            NavigationBarItem(
                                selected = currentDestination == "places" ,
                                onClick = {navController.navigate("places")},
                                icon = {Icon(
                                    imageVector =
                                    if(currentDestination == "places") Icons.AutoMirrored.Filled.FormatListBulleted else Icons.AutoMirrored.Outlined.FormatListBulleted,
                                    contentDescription = null
                                )},
                                label ={
                                    Text("Places",
                                        style = MaterialTheme.typography.titleSmall
                                        )
                                },
                                alwaysShowLabel = true,
                                colors = NavigationBarItemDefaults.colors(selectedIconColor = Primary,
                                    selectedTextColor = Primary,
                                    unselectedIconColor = Color.Gray,
                                    unselectedTextColor = Color.Gray,
                                    indicatorColor = Color.Gray.copy(alpha = 0.1f))
                            )
                        }
                    }
                ) { innerPadding ->
                    NavGraph(navController = navController , modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

