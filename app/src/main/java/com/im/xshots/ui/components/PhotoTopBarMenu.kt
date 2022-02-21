package com.im.xshots.ui.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun PhotoTopBarMenu(
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    url: String,
    context: Context,
) {

    val expanded = remember { mutableStateOf(false) }
    val save = remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            expanded.value = true
        }
    ) {
        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "")
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier.fillMaxWidth(0.75f),
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
        SavePhoto(scaffoldState, scope, url, context)
        save.value = false
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SavePhoto(scaffoldState: ScaffoldState, scope: CoroutineScope, url: String, context: Context) {

    val permissionsState =
        rememberPermissionState(permission = Manifest.permission.WRITE_EXTERNAL_STORAGE)

    val lifeCycle = LocalLifecycleOwner.current

    DisposableEffect(
        key1 = lifeCycle,
        effect = {

            val observer = LifecycleEventObserver { _, event ->

                if (event == Lifecycle.Event.ON_RESUME) {
                    permissionsState.launchPermissionRequest()
                }

            }

            lifeCycle.lifecycle.addObserver(observer)

            onDispose {
                lifeCycle.lifecycle.removeObserver(observer)
            }

        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (permissionsState.permission) {

            Manifest.permission.WRITE_EXTERNAL_STORAGE -> {

                when {

                    permissionsState.hasPermission -> {


                        DownloadPhoto(scaffoldState, scope, url, context)

                    }

                    permissionsState.shouldShowRationale -> {

                        LaunchedEffect(scope) {

                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("External storage permission is needed to save photo.")
                            }

                        }

                    }

                    !permissionsState.hasPermission && !permissionsState.shouldShowRationale -> {

                        LaunchedEffect(scope) {

                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("External storage permission was permanently denied. Go to app settings and enable permission.")
                            }

                        }

                    }

                }

            }

        }

    }

}


@SuppressLint("Range")
@Composable
fun DownloadPhoto(
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    url: String,
    context: Context,
) {

    downloadPhoto(scaffoldState, scope, url, context)

}

