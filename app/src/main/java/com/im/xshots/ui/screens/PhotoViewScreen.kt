package com.im.xshots.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.im.xshots.R
import com.im.xshots.ui.components.ImageLoader
import com.im.xshots.ui.components.PhotoTopBarMenu
import com.im.xshots.ui.util.Screen
import kotlinx.coroutines.CoroutineScope


@Composable
fun PhotoViewScreen(
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

        val imageSelected = url?.let {
            ImageLoader(uri = it,
                resource = R.drawable.placeholder,
                context = context).value
        }
        imageSelected.let { selected ->
            selected?.let { it ->
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                PhotoTopBar(navController,scaffoldState, scope, url, context)
            },
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.Transparent,
            contentColor = Color.White,
        ) {}


    }

}

@Composable
fun PhotoTopBar(
    navController: NavController,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    url: String?,
    context: Context
) {

        TopAppBar(
            title = {

            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        navController.navigate(Screen.PhotosScreen.route)
                    }
                ) {
                    Icon(Icons.Filled.ArrowBack,
                        "backIcon",
                        modifier = Modifier.size(30.dp))
                }
            },
            actions = {
                url?.let { PhotoTopBarMenu(scaffoldState, scope, it, context) }
            },
            backgroundColor = Color.Transparent,
            modifier = Modifier.fillMaxWidth()

        )

}