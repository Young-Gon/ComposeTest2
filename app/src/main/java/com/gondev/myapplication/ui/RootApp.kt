package com.gondev.myapplication.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gondev.myapplication.theme.ComposeTest2Theme
import com.gondev.myapplication.ui.favorite.FavoriteScreen
import com.gondev.myapplication.ui.home.HomeScreen
import com.gondev.myapplication.ui.login.loginNavGraph
import com.gondev.myapplication.ui.profile.ProfileScreen
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootApp() {
    // A surface container using the 'background' color from the theme
    val appState = rememberAppState()
    Surface(modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background) {

        val navController = appState.navController
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = currentDestination?.route ?: "")
                    },
                    actions = {
                        Surface(
                            modifier = Modifier,
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                        ) {
                            IconButton(onClick = {
                                navController.navigate(NavPath.Profile.route)
                            }) {
                                Icon(Icons.Filled.Person,
                                    contentDescription = null,
                                    tint = Color.White)
                            }
                        }
                    })
            },
            bottomBar = {
                val homeScreenMenu = listOf<BottomNavigationBarMenu>(NavPath.Home, NavPath.Favorite)
                if (navBackStackEntry?.destination?.route in homeScreenMenu.map { it.route })
                    NavigationBar {
                        for (screen in homeScreenMenu) {
                            NavigationBarItem(
                                icon = { Icon(screen.icon, null) },
                                label = { Text(screen.route) },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    navController.navigate(screen.route) {

                                        // Pop up to the start destination of the graph to
                                        // avoid building up a large stack of destinations
                                        // on the back stack as users select items
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        // Avoid multiple copies of the same destination when
                                        // reselecting the same item
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
            },
        ) { innerPadding ->
            NavHost(navController = navController,
                startDestination = NavPath.Home.route,
                modifier = Modifier.padding(innerPadding)) {
                composable(NavPath.Home.route) {
                    HomeScreen(hiltViewModel())
                }
                composable(NavPath.Favorite.route) {
                    FavoriteScreen()
                }
                composable(NavPath.Profile.route) {
                    ProfileScreen(appState.navController)
                }
                loginNavGraph()
            }
        }
    }
}

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) = remember(navController, coroutineScope) {
    AppState(navController, coroutineScope)
}

class AppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope
) {

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeTest2Theme {
        RootApp()
    }
}