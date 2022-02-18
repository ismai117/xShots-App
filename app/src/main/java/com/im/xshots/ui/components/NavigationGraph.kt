package com.im.xshots.ui.components

import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.im.xshots.ui.screens.PhotoViewScreen
import com.im.xshots.ui.screens.PhotosScreen
import com.im.xshots.ui.screens.VideoViewScreen
import com.im.xshots.ui.screens.VideosScreen
import com.im.xshots.ui.util.Screen
import com.im.xshots.ui.viewmodel.PhotosViewModel
import com.im.xshots.ui.viewmodel.VideosViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun NavigationGraph(
    navController: NavController,
    bottomNavState: MutableState<Boolean>,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    photosModel: PhotosViewModel,
    videosModel: VideosViewModel,
    lazyListState: LazyListState,
    context: Context,
) {

    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.PhotosScreen.route
    ) {
        composable(route = Screen.PhotosScreen.route) {
            LaunchedEffect(Unit) {
                bottomNavState.value = true
            }
            PhotosScreen(navController, scaffoldState, scope,
                photosModel, lazyListState, context)
        }
        composable(
            route = Screen.ImageViewScreen.route + "/{image}",
            arguments = listOf(navArgument("image") { type = NavType.StringType })
        ) { backStackEntry ->
            LaunchedEffect(Unit) {
                bottomNavState.value = false
            }
            PhotoViewScreen(
                navController = navController,
                scaffoldState = scaffoldState,
                url = backStackEntry.arguments?.getString("image"),
                context = context)
        }
        composable(
            route = Screen.VideosScreen.route
        ) {
            LaunchedEffect(Unit) {
                bottomNavState.value = true
            }
            VideosScreen(
                navController, scaffoldState, scope,
                videosModel, lazyListState, context
            )
        }
        composable(
            route = Screen.VideoViewScreen.route + "/{video}",
            arguments = listOf(navArgument("video") { type = NavType.StringType })
        ) { backStackEntry ->
            LaunchedEffect(Unit) {
                bottomNavState.value = false
            }
            VideoViewScreen(
                scaffoldState = scaffoldState,
                navController = navController,
                url = backStackEntry.arguments?.getString("video"),
                context = context
            )
        }
    }

}
