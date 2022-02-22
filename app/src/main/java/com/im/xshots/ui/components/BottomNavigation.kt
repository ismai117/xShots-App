package com.im.xshots.ui.components

import android.widget.Space
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.im.xshots.ui.util.Screen


@Composable
fun BottomNav(
    navController: NavController,
    bottomNavState: MutableState<Boolean>,
) {
    val items = listOf(
        Screen.PhotosScreen,
        Screen.VideosScreen
    )

    AnimatedVisibility(
        visible = bottomNavState.value,

    ) {

        BottomNavigation(
            backgroundColor = Color.White,
            elevation = 12.dp,
        ) {

            val bottomNavBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = bottomNavBackStackEntry?.destination?.route

            items.forEach { item ->

                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.title
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            fontSize = 12.sp,
                        )
                    },
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
                    },
                    selectedContentColor = Color.Black,
                    unselectedContentColor = Color.LightGray,
                    alwaysShowLabel = true,
                )

            }

        }

    }

}


