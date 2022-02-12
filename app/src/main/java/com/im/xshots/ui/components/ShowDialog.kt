package com.im.xshots.ui.components

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.im.xshots.R


fun showDialog(context: Context) {
    MaterialAlertDialogBuilder(context)
        .setIcon(R.drawable.ic_folder)
        .setMessage("Allow xShots to access photos, media and files on your device?")
        .setNegativeButton("Deny") { dialog, which ->

        }
        .setPositiveButton("Allow") { dialog, which ->
            ActivityCompat.requestPermissions(context as Activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1)
        }
        .show()
}

