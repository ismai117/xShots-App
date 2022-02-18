package com.im.xshots.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.im.xshots.model.videos.Videos
import com.im.xshots.ui.util.Screen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun VideoList(
    navController: NavController,
    videos: List<Videos>,
    listState: LazyListState,
    context: Context,
) {

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {

        itemsIndexed(
            items = videos
        ) { index, video ->

            VideoCard(
                videos = video,
                onClick = {
                    val encodedUrl = URLEncoder.encode("${video.video_files.get(0).link}", StandardCharsets.UTF_8.toString())
                    navController.navigate(Screen.VideoViewScreen.route + "/$encodedUrl")
                },
                context = context
            )

        }

        item {
            Spacer(modifier = Modifier.padding(8.dp))
        }

    }

}