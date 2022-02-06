package com.im.xshots.ui.components

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.im.xshots.ui.util.Screen
import java.io.File

@Composable
fun TopBarMenu(
    url: String?,
    context: Context,
    navController: NavController,
    onClick: (String) -> Unit
) {

    val expanded = remember { mutableStateOf(false) }
    val save = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(
            onClick = { expanded.value = true }
        ) {
            Icon(
                Icons.Filled.MoreVert,
                contentDescription = "App Bar Menu"
            )
        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded.value = false
                    save.value = true
                    url?.let {
                        onClick(url)
                    } ?: ""
                },
            ) {
                Text(
                    text = "Save",
                )
            }
            DropdownMenuItem(
                onClick = {
                    expanded.value = false
                    navController.navigate(Screen.DownloadedScreen.route)
                }
            ) {
                Text(text = "Downloaded")
            }
        }
    }

    if (save.value) {
        Save(url, context)
        save.value = false
    }

}

@Composable
fun Save(url: String?, context: Context) {
    if (
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
        Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
    ) {
        AskPermission(url = url, context = context)
    } else {
        DownloadImage(url = url, context = context)
    }
}

@Composable
fun AskPermission(url: String?, context: Context) {

    if (
        ContextCompat.checkSelfPermission(context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
    ) {
        if (
            ActivityCompat.shouldShowRequestPermissionRationale(context as Activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ) {
            ShowDialog(url = url, context = context)
        } else {
            ActivityCompat.requestPermissions(context,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1)
        }
    } else {
        DownloadImage(url = url, context = context)
    }

}

@Composable
fun DownloadImage(url: String?, context: Context) {

    val isDownloading = remember { mutableStateOf(false) }
    val lastMessage = remember { mutableStateOf("") }

    val dir = File(Environment.DIRECTORY_PICTURES)

    if (!dir.exists()) {
        dir.exists()
    }

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    val downloadUri = Uri.parse(url)

    val request = DownloadManager.Request(downloadUri).apply {

        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(false)
            .setTitle(url?.substring(url.lastIndexOf("/") + 1))
            .setDescription("")
            .setDestinationInExternalPublicDir(
                dir.toString(),
                url?.substring(url.lastIndexOf("/") + 1)
            )

    }

    val downloadId = downloadManager.enqueue(request)

    val query = DownloadManager.Query().setFilterById(downloadId)


    Thread(Runnable {

        while (isDownloading.value) {


            val cursor: Cursor = downloadManager.query(query)

            cursor.moveToFirst()

            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    .toInt()) == DownloadManager.STATUS_SUCCESSFUL
            ) {
                isDownloading.value = false
            }

            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS).toInt())

            val message = statusMessage(url, dir, status)

            if (message != lastMessage.value){

                (context as Activity).runOnUiThread{

                    Log.d("status_download", "$message")

                }

                lastMessage.value = message ?: ""
            }

            cursor.close()

        }

    }).start()

}

fun statusMessage(url: String?, dir: File, status: Int): String {

    val msg = mutableStateOf("")

    msg.value = when (status) {

        DownloadManager.STATUS_SUCCESSFUL -> "Download status is successful in $dir" + File.separator + url?.substring(url.lastIndexOf("/") + 1)

        DownloadManager.STATUS_RUNNING -> "Download status is running"

        DownloadManager.STATUS_PAUSED -> "Download status is paused"

        DownloadManager.STATUS_PENDING -> "Download status is pending"

        DownloadManager.STATUS_FAILED -> "Download status is failed"

        else -> {
            "There's nothing to download"
        }

    }

    return msg.value

}


@Composable
fun ShowDialog(
    url: String?,
    context: Context,
) {

    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        Dialog(
            onDismissRequest = { openDialog.value = false },
        ) {
            val dialogWidth = 2400.dp
            val dialogHeight = 180.dp

            Box(
                modifier = Modifier
                    .width(dialogWidth)
                    .height(dialogHeight)
                    .background(Color.White)
            ) {
                Text(
                    text = "Permission Required",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(20.dp)
                )
                Text(
                    text = "Permission required to save photo from the Web.",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp
                    ),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 20.dp, top = 60.dp)
                )
                Row(modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            openDialog.value = false
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                    ) {
                        Text(
                            text = "CANCEL",
                            color = Color.Blue,
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    OutlinedButton(
                        onClick = {
                            openDialog.value = false
                            ActivityCompat.requestPermissions(context as Activity,
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                1)
                            Log.d("saveimage", "$url")
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                    ) {
                        Text(
                            text = "ALLOW",
                            color = Color.Blue,
                        )
                    }
                }
            }
        }
    }

}

