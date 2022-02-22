package com.im.xshots.ui.screens

import android.content.Context
import android.util.Log
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
import com.im.xshots.ui.components.ProgressBar
import com.im.xshots.ui.components.VideoList
import com.im.xshots.ui.theme.Fonts
import com.im.xshots.ui.util.NetworkState
import com.im.xshots.ui.viewmodel.VideosViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun VideosScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    videosModel: VideosViewModel,
    listState: LazyListState,
    context: Context,
) {

    val query = videosModel.query.value
    val videos = videosModel.videos.value
    val softKeyboardController = LocalSoftwareKeyboardController.current


    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Discover Video's",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 28.sp,
                        fontFamily = Fonts
                    ),
                    modifier = Modifier
                        .padding(
                            start = 20.dp,
                            top = 40.dp
                        )
                )

                Spacer(modifier = Modifier.padding(19.dp))

                TextField(
                    value = query,
                    onValueChange = {
                        videosModel.onChangedQuery(it)
                    },
                    label = {
                        Text(text = "Search")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    shape = RoundedCornerShape(50.dp),
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "")
                    },
                    keyboardActions = KeyboardActions(onSearch = {
                        if (query != "") {
                            videosModel.searchVideos(query = query)
                        } else {
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("Empty value is not allowed!")
                            }
                        }
                        softKeyboardController?.hide()
                    }),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search,
                        keyboardType = KeyboardType.Text
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

            when (videos) {

                is NetworkState.Success -> {

                    videos.data?.let {
                        VideoList(navController = navController,
                            videos = it,
                            listState = listState,
                            context = context)
                    }

                }

                is NetworkState.Error -> {


                    LaunchedEffect(scope) {

                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("${videos.error?.message}")
                        }

                    }

                    Log.d("video", "${videos.error?.message}")


                }

                is NetworkState.Loading -> {

                    ProgressBar(isDisplayed = true)

                }

                else -> {}

            }


        }

    }

}