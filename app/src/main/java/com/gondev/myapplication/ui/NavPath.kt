package com.gondev.myapplication.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

interface BottomNavigationBarMenu {
    val route: String
    val icon: ImageVector
}

sealed class NavPath(
    val route: String,
) {
    object Home : NavPath(route = "Home"), BottomNavigationBarMenu {
        override val icon: ImageVector = Icons.Filled.Home
    }

    object Favorite : NavPath(route = "Favorite"), BottomNavigationBarMenu {
        override val icon: ImageVector = Icons.Filled.Favorite
    }

    object Profile: NavPath(route = "Profile")
    object Login: NavPath(route = "login")
}
