package com.im.xshots.ui.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.im.xshots.ui.components.VideoTopBarMenu
import com.im.xshots.ui.util.Screen


@Composable
fun VideoViewScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    url: String?,
    context: Context,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {


        Log.d("videoLink", "$url")

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                VideoTopBar(navController, url, context)
            },
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.Transparent
        ) {
            VideoPlayer(url, context)
        }

    }

}

@Composable
fun VideoTopBar(
    navController: NavController,
    url: String?,
    context: Context,
) {

    TopAppBar(
        title = {

        },
        navigationIcon = {
            IconButton(
                onClick = { navController.navigate(Screen.VideosScreen.route) }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = ""
                )
            }
        },
        actions = {
            url?.let { VideoTopBarMenu(url, context) }
        },
        backgroundColor = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    )

}


@Composable
fun VideoPlayer(url: String?, context: Context) {

    val uri = Uri.parse(url)

    val exoPlayer = remember(context) {
        SimpleExoPlayer.Builder(context).build().apply {
            val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(context,
                Util.getUserAgent(context, context.packageName))

            val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)

            this.prepare(source)
        }
    }

    DisposableEffect(
        AndroidView(
            modifier =
            Modifier
                .testTag("VideoPlayer")
                .fillMaxSize(),
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                    layoutParams =
                        FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams
                                .MATCH_PARENT,
                            ViewGroup.LayoutParams
                                .MATCH_PARENT,
                        )
                }
            }
        )
    ) {
        onDispose {
            exoPlayer.release()
        }
    }


}
