package com.im.xshots.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.im.xshots.ui.components.*
import com.im.xshots.ui.viewmodel.PhotosViewModel
import com.im.xshots.ui.viewmodel.VideosViewModel
import dagger.hilt.android.AndroidEntryPoint



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
        val topBarState = remember { mutableStateOf(false) }
        val bottomNavState = rememberSaveable() { mutableStateOf(false) }

        Scaffold(
            bottomBar = {
                BottomNav(
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






