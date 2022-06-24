package com.gondev.myapplication.ui.profile

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gondev.myapplication.ui.NavPath

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val token by viewModel.token.collectAsState(initial = null)
    if (token == null) {
        navController.navigate(route = NavPath.Login.route)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    navBackStackEntry?.savedStateHandle?.let { savedStateHandle ->
        if (savedStateHandle.contains("loginResult")) {
            val loginSuccess by savedStateHandle.getStateFlow("loginResult", true).collectAsState()
            if(!loginSuccess) {
                navController.graph.findStartDestination().route?.let { startDestination ->
                    navController.navigate(startDestination){

                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                }
            }
        }
    }

    Text(text = "PROFILE")
}