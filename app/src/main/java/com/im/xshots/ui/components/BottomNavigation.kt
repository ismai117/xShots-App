package com.im.xshots.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.im.xshots.ui.util.Screen


@Composable
fun BottomNavigation(
    navController: NavController,
    bottomNavState: MutableState<Boolean>,
) {
    val items = listOf(
        Screen.PhotosScreen,
        Screen.VideosScreen
    )

    AnimatedVisibility(
        visible = bottomNavState.value
    ) {
        androidx.compose.material.BottomNavigation(
            backgroundColor = Color.White,
            elevation = 8.dp
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(painterResource(id = item.icon),
                            contentDescription = item.title)
                    },
                    label = {
                        Text(text = item.title,
                            fontSize = 9.sp)
                    },
                    selectedContentColor = Color.Black,
                    unselectedContentColor = Color.LightGray,
                    alwaysShowLabel = true,
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {

                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

}


