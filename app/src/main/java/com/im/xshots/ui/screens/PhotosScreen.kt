package com.im.xshots.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.im.xshots.ui.components.PhotoList
import com.im.xshots.ui.components.ProgressBar
import com.im.xshots.ui.theme.Fonts
import com.im.xshots.ui.util.NetworkState
import com.im.xshots.ui.viewmodel.PhotosViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PhotosScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    photosModel: PhotosViewModel,
    listState: LazyListState,
    context: Context,
) {

    val query = photosModel.query.value
    val photos = photosModel.photos.value
    val softKeyboardController = LocalSoftwareKeyboardController.current


    Scaffold(
        scaffoldState = scaffoldState,
    ) {
        Column {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Discover Photo's",
                    style = TextStyle(
                        color = Color.Black,
                        fontFamily = Fonts,
                        fontSize = 28.sp
                    ),
                    modifier = Modifier.padding(top = 40.dp, start = 20.dp)
                )

                Spacer(modifier = Modifier.padding(19.dp))

                TextField(
                    value = query,
                    onValueChange = {
                        photosModel.onQueryChanged(query = it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    label = {
                        Text(text = "Search")
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "")
                    },
                    shape = RoundedCornerShape(50.dp),
                    keyboardActions = KeyboardActions(onSearch = {
                        if (query != "") {
                            photosModel.searchPhotos(query = query)
                            scope.launch {
                                listState.scrollToItem(index = 0)
                            }
                        } else {
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("Empty value is not allowed!")
                            }
                        }
                        softKeyboardController?.hide()
                    }),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color.Black,
                        cursorColor = Color.Black
                    ),
                    maxLines = 1,
                    singleLine = true
                )

            }

            Spacer(modifier = Modifier.padding(16.dp))

            when (photos) {

                is NetworkState.Success -> {

                    photos.data?.let { it ->
                        PhotoList(navController = navController,
                            images = it,
                            listState,
                            context = context)
                    }

                }

                is NetworkState.Error -> {

                    LaunchedEffect(scope) {

                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("${photos.error?.message}")
                        }

                    }

                }

                is NetworkState.Loading -> {

                    ProgressBar(isDisplayed = true)

                }


                else -> {}
            }


        }

    }

}