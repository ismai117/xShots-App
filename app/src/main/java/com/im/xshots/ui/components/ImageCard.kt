package com.im.xshots.ui.components

import android.content.Context
import android.view.RoundedCorner
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.im.xshots.R
import com.im.xshots.model.images.Images


@Composable
fun ImageCard(
    images: Images,
    onClick: () -> Unit,
    context: Context,
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(start = 22.dp, end = 22.dp),
        elevation = 8.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            val image = ImageLoader(uri = images.portrait,
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