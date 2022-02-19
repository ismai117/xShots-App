package com.im.xshots.ui.components

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope


@Composable
fun VideoTopBarMenu(
    scaffold: ScaffoldState,
    scope: CoroutineScope,
    url: String,
    context: Context,
) {

    val expanded = remember { mutableStateOf(false) }
    val save = remember { mutableStateOf(false) }

    IconButton(
        onClick = { expanded.value = true }
    ) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = ""
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    save.value = true
                }
            ) {
                Text(text = "Save")
            }
        }
    }

    if (save.value) {
        SaveVideo(scaffold, scope, url, context)
        save.value = false
    }

}

@Composable
fun SaveVideo(scaffold: ScaffoldState, scope: CoroutineScope, url: String, context: Context) {
    if (
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
        Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
    ) {
        AskPermissionForVideoDownload(scaffold, scope, url, context)
    }
    else {
        DownloadVideos(scaffold, scope, url, context)
    }
}


@Composable
fun AskPermissionForVideoDownload(scaffold: ScaffoldState, scope: CoroutineScope, url: String, context: Context) {
    if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
        if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            showDialog(context = context)
        } else {
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
    } else{
        DownloadVideos(scaffold, scope, url, context)
    }
}

@Composable
fun DownloadVideos(scaffold: ScaffoldState, scope: CoroutineScope, url: String, context: Context) {

    DownloadVideo(scaffold, scope, url, context)

}

