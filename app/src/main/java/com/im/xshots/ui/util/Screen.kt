package com.im.xshots.ui.util

sealed class Screen(val route: String){

    object SearchScreen : Screen("search_screen")

    object ImageScreen : Screen("image_screen")

}
