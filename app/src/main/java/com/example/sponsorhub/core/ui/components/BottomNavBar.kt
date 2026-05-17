package com.example.sponsorhub.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sponsorhub.navigation.Routes

data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun BottomNavBar(
    navController: NavHostController,
    role: String
) {

    val navBackStackEntry =
        navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry.value
            ?.destination
            ?.route

    val items = when (role) {

        "panitia" -> listOf(

            BottomNavItem(
                title = "Event",
                route = Routes.EVENT_LIST,
                icon = Icons.Default.LocationOn
            ),

            BottomNavItem(
                title = "Article",
                route = Routes.ARTICLE_LIST,
                icon = Icons.Default.AccountBox
            ),

            BottomNavItem(
                title = "Profile",
                route = Routes.PROFILE,
                icon = Icons.Default.Person
            )
        )

        "umkm" -> listOf(

            BottomNavItem(
                title = "Event",
                route = Routes.EVENT_LIST,
                icon = Icons.Default.LocationOn
            ),

            BottomNavItem(
                title = "Catalog",
                route = Routes.CATALOG,
                icon = Icons.Default.Menu
            ),

            BottomNavItem(
                title = "Profile",
                route = Routes.PROFILE,
                icon = Icons.Default.Person
            )
        )

        else -> emptyList()
    }

    NavigationBar {

        items.forEach { item ->

            NavigationBarItem(

                selected =
                    currentRoute == item.route,

                onClick = {

                    navController.navigate(
                        item.route
                    ) {

                        popUpTo(
                            navController.graph.startDestinationId
                        )

                        launchSingleTop = true
                    }
                },

                icon = {

                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },

                label = {

                    Text(item.title)
                }
            )
        }
    }
}