package com.im.xshots.ui.service

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.im.xshots.R
import okhttp3.internal.notify

class DownloadBroadcast : BroadcastReceiver() {

    private val channelID = "1"
    private val channelName = "xShots_download_notification"
    private val notificationId = 1

    companion object {
        var id: Long? = null
        private var downloadId: Long? = null
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        downloadId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

        if (id == downloadId) {

            context?.let { showNotification(context = it) }

        }

    }


    private fun showNotification(context: Context) {

        createNotificationChannel(context = context)

        val notification = NotificationCompat.Builder(
            context, channelID
        ).apply {

            setSmallIcon(R.drawable.xshots_logo)
            setContentTitle("xShots")
            setContentText("Download Successfully Completed")
            setAutoCancel(true)
            setPriority(NotificationCompat.PRIORITY_DEFAULT)

        }.build()


        val notificationManager = NotificationManagerCompat.from(context)

        notificationManager.notify(notificationId, notification)

    }


    private fun createNotificationChannel(context: Context?) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                channelID,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }

            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)

        }


    }


}