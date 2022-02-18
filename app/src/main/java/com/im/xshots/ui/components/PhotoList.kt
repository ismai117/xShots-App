package com.im.xshots.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.im.xshots.model.photos.Photos
import com.im.xshots.ui.util.Screen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun PhotoList(
    navController: NavController,
    images: List<Photos>,
    listState: LazyListState,
    context: Context
) {


    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {

        itemsIndexed(
            items = images
        ){ index, image ->

            PhotoCard(
                photos = image,
                onClick = {
                    val encodedUrl = URLEncoder.encode("${image.portrait}", StandardCharsets.UTF_8.toString())
                    navController.navigate(Screen.ImageViewScreen.route + "/$encodedUrl")
                },
                context = context
            )


        }



        item { 
            Spacer(modifier = Modifier.padding(8.dp))
        }


    }




}

