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


@Composable
fun VideoTopBarMenu(
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
        Toast.makeText(context, "saved", Toast.LENGTH_LONG).show()
        SaveVideo(url, context)
        save.value = false
    }

}

@Composable
fun SaveVideo(url: String, context: Context) {
    if (
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
        Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
    ) {
        AskPermissionForVideoDownload(url = url, context = context)
    }
    else {
        DownloadVideo(url, context)
    }
}


@Composable
fun AskPermissionForVideoDownload(url: String, context: Context) {
    if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
        if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            showDialog(context = context)
        } else {
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
    } else{
        DownloadVideo(url = url, context = context)
    }
}

@Composable
fun DownloadVideo(url: String, context: Context) {

    downloadVideo(url, context)

}

