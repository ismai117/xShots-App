package com.im.xshots.ui.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.im.xshots.R
import com.im.xshots.model.photos.Photos


@Composable
fun PhotoCard(
    photos: Photos,
    onClick: () -> Unit,
    context: Context,
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(440.dp)
            .clickable { onClick() }
            .padding(start = 22.dp, end = 22.dp),
        elevation = 8.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val image = ImageLoader(uri = photos.portrait,
                resource = R.drawable.placeholder,
                context = context)
            image.value?.asImageBitmap()?.let {
                Image(
                    bitmap = it,
                    contentDescription = "",
                    modifier = Modifier
                        .height(470.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop)
            }

        }
    }
}