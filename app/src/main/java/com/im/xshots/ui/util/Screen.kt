package com.im.xshots.ui.util

import com.im.xshots.R

sealed class Screen(val title: String, val icon: Int, val route: String){

    object PhotosScreen : Screen("Photos", R.drawable.ic_pictures,"photos_screen")

    object ImageViewScreen : Screen("", 0,"imageview_screen")

    object VideosScreen : Screen("Videos", R.drawable.ic_video,"videos_screen")

    object VideoViewScreen : Screen("", 0, "videoview_screen")

}
