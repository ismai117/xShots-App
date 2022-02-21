package com.im.xshots.ui.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.border
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.im.xshots.ui.components.VideoTopBarMenu
import com.im.xshots.ui.util.Screen
import kotlinx.coroutines.CoroutineScope


@Composable
fun VideoViewScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    url: String?,
    context: Context,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {


        Log.d("videoLink", "$url")

        VideoPlayer(url, context)

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                VideoTopBar(navController, scaffoldState, scope, url, context)
            },
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.Transparent,
        ) {}

    }

}

@Composable
fun VideoTopBar(
    navController: NavController,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
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
            url?.let { VideoTopBarMenu(scaffoldState, scope, url , context) }
        },
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.Transparent,
        contentColor = Color.White
    )

}


@Composable
fun VideoPlayer(url: String?, context: Context) {

    val uri = Uri.parse(url)

    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context)
            .build().apply {
            val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(context,
                Util.getUserAgent(context, context.packageName))

            val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)

            this.prepare(source)
        }
    }

    exoPlayer.playWhenReady = true
    exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

    DisposableEffect(
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
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

