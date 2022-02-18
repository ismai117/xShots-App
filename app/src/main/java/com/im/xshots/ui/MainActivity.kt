package com.im.xshots.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.im.xshots.R
import com.im.xshots.ui.components.*
import com.im.xshots.ui.screens.PhotoViewScreen
import com.im.xshots.ui.screens.PhotosScreen
import com.im.xshots.ui.screens.VideoViewScreen
import com.im.xshots.ui.screens.VideosScreen

import com.im.xshots.ui.theme.Fonts
import com.im.xshots.ui.util.NetworkState
import com.im.xshots.ui.util.Screen

import com.im.xshots.ui.viewmodel.PhotosViewModel
import com.im.xshots.ui.viewmodel.VideosViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val photosModel: PhotosViewModel by viewModels()
    private val videosModel: VideosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            photosModel.screenIsLoading.value
        }

        setContent {

            MaterialTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(photosModel, videosModel, this)
                }
            }

        }


    }

    @Composable
    fun MainScreen(searchModel: PhotosViewModel, videosModel: VideosViewModel, context: Context) {

        val navController = rememberNavController()
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        val lazyListState = rememberLazyListState()
        val bottomNavState = rememberSaveable() {
            mutableStateOf(false)
        }

        Scaffold(
            bottomBar = {
                BottomNavigation(
                    navController = navController,
                    bottomNavState = bottomNavState
                )
            },
        ) { innerPadding ->
            Box(
                modifier = Modifier.padding(innerPadding)
            ) {
                NavigationGraph(
                    navController = navController,
                    bottomNavState = bottomNavState,
                    scaffoldState = scaffoldState,
                    scope = scope,
                    photosModel = searchModel,
                    videosModel = videosModel,
                    lazyListState = lazyListState,
                    context = context)
            }
        }

    }






}






