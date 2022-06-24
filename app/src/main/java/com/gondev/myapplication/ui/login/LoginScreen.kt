package com.gondev.myapplication.ui.login

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

@Composable
fun LoginScreen(navBackStackEntry: NavBackStackEntry) {
    Text(text = "PROFILE")
}


fun NavGraphBuilder.loginNavGraph() {
    navigation(
        route = "login",
        startDestination = "login/login"
    ) {
        composable("login/login") {
            LoginScreen(it)
        }
    }
}