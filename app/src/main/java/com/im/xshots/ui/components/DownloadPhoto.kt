package com.im.xshots.ui.components

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.mutableStateOf
import com.im.xshots.ui.service.DownloadBroadcast
import com.im.xshots.ui.service.DownloadBroadcast.Companion.id
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File


@SuppressLint("Range")
fun downloadPhoto(
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    url: String?,
    context: Context
){


    val lastMessage = mutableStateOf("")

    val dir = File(Environment.DIRECTORY_PICTURES)

    if(!dir.exists()){
        dir.mkdirs()
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
    val query =  DownloadManager.Query().setFilterById(downloadId)


    // broadcastReceiver downloadid

    id = downloadId

    Thread(Runnable {

        var isDownloading = true

        while (isDownloading){

            val cursor: Cursor = downloadManager.query(query)

            cursor.moveToFirst()

            if(cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL){

                isDownloading = false

                (context as Activity).runOnUiThread {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("Download status is successful")
                    }
                }

            }

            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

            val message = photosStatusMessage(status)

            if (message != lastMessage.value){

                (context as Activity).runOnUiThread {
                    Log.d("status_message", "$message")
                    lastMessage.value = message ?: ""
                }

            }

            cursor.close()

        }

    }).start()







}

fun photosStatusMessage(status: Int): String{

    var msg = ""

    msg = when(status){

        DownloadManager.STATUS_SUCCESSFUL -> "Download status is successful"

        DownloadManager.STATUS_PENDING -> "Download status is pending"

        DownloadManager.STATUS_RUNNING -> "Download status is running"

        DownloadManager.STATUS_PAUSED -> "Download status is paused"

        DownloadManager.STATUS_FAILED -> "Download status is failed"

        else -> "There's nothing to download"
    }

    return msg

}
