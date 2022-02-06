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
import com.im.xshots.model.images.Images
import com.im.xshots.ui.util.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun ImageList(
    navController: NavController,
    listState: LazyListState,
    images: List<Images>,
    context: Context
) {

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        
        itemsIndexed(
            items = images,
        ) { index, image ->

            ImageCard(
                images = image,
                onClick = {
                    val encodedUrl = URLEncoder.encode("${image.portrait}", StandardCharsets.UTF_8.toString())
                    navController.navigate(Screen.ImageScreen.route + "/$encodedUrl")
                },
                context = context
            )

        }
        
        item { 
            Spacer(modifier = Modifier.padding(8.dp))
        }

    }
}