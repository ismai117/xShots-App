package com.im.xshots.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.runtime.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition


@Composable
fun ImageLoader(
    url: String,
    @DrawableRes resource: Int,
    context: Context
): MutableState<Bitmap?>{

    val imageState: MutableState<Bitmap?> = remember { mutableStateOf(null) }

    LaunchedEffect(url){

    Glide.with(context).asBitmap().load(resource).into(object : CustomTarget<Bitmap>(){

        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
             imageState.value = resource
        }

        override fun onLoadCleared(placeholder: Drawable?) {

        }

    })



    Glide.with(context).asBitmap().load(url).into(object : CustomTarget<Bitmap>(){

        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            imageState.value = resource
        }

        override fun onLoadCleared(placeholder: Drawable?) {

        }

    })

    }

    return imageState

}