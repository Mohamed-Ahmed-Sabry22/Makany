package com.kabo.a24_makany.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kabo.a24_makany.ui.screens.home.MapHomeScreen
import com.kabo.a24_makany.ui.screens.places.PlacesScreen
import com.kabo.a24_makany.ui.screens.auth.LoginScreen
import com.kabo.a24_makany.ui.screens.auth.SignUpScreen

@Composable
fun NavGraph(navController: NavHostController , modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("login") { LoginScreen() }
        composable("signup") { SignUpScreen() }
        composable("home") { MapHomeScreen() }
        composable("places") { PlacesScreen() }
    }
}