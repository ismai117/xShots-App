package com.im.xshots.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.im.xshots.R
import com.im.xshots.model.images.DownloadedImages
import com.im.xshots.ui.components.*

import com.im.xshots.ui.theme.Fonts
import com.im.xshots.ui.util.NetworkState
import com.im.xshots.ui.util.Screen

import com.im.xshots.ui.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val searchModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            searchModel.screenIsLoading.value
        }

        setContent {

            MaterialTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Navigation(searchModel, this)
                }
            }

        }


    }


    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun Navigation(
        searchModel: SearchViewModel,
        context: Context
    ) {

        val navController = rememberNavController()
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        val lazyListState = rememberLazyListState()


        NavHost(
            navController = navController,
            startDestination = Screen.SearchScreen.route
        ) {
            composable(route = Screen.SearchScreen.route) {
                SearchScreen(navController = navController, scaffoldState, scope,
                    searchModel, lazyListState, context)
            }
            composable(
                route = Screen.ImageScreen.route + "/{image}",
                arguments = listOf(navArgument("image") { type = NavType.StringType })
            ) { backStackEntry ->
                ImageScreen(
                    navController = navController,
                    scaffoldState = scaffoldState,
                    backStackEntry.arguments?.getString("image"),
                context)
            }
        }

    }


    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun SearchScreen(
        navController: NavController,
        scaffoldState: ScaffoldState,
        scope: CoroutineScope,
        searchModel: SearchViewModel,
        listState: LazyListState,
        context: Context
    ) {

        val query = searchModel.query.value
        val images = searchModel.images.value
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
                            searchModel.onQueryChanged(query = it)
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
                                searchModel.searchImage(query = query)
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
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                }

                Spacer(modifier = Modifier.padding(16.dp))

                when (images) {

                    is NetworkState.Success -> {

                        images.data?.let { it ->
                            ImageList(navController = navController,
                                images = it,
                                listState,
                                context = context)
                        }

                    }

                    is NetworkState.Error -> {

                        Toast.makeText(context,
                            "${images.error?.message}",
                            Toast.LENGTH_LONG).show()

                    }

                    is NetworkState.Loading -> {

                        ProgressBar(isDisplayed = true)

                    }


                    else -> {}
                }


            }

        }

    }


    @Composable
    fun ImageScreen(
        navController: NavController,
        scaffoldState: ScaffoldState,
        url: String?,
        context: Context
    ) {

        Scaffold(
            scaffoldState = scaffoldState
        ) {

            Box(
                modifier = Modifier.fillMaxSize()
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
                        TopBar(navController = navController, url, context)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color.Transparent,
                    contentColor = Color.White
                ) {}
            }

        }

    }


    @Composable
    fun TopBar(navController: NavController, url: String?, context: Context) {

        TopAppBar(
            title = {

            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        navController.navigate(Screen.SearchScreen.route)
                    }
                ) {
                    Icon(Icons.Filled.ArrowBack,
                        "backIcon",
                        modifier = Modifier.size(30.dp))
                }
            },
            actions = {
                url?.let { ShowMenu(navController, it, context) }
            },
            backgroundColor = Color.Transparent,
            modifier = Modifier.fillMaxWidth()
        )


    }

    private @Composable
    fun ShowMenu(
        navController: NavController,
        url: String,
        context: Context
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
                DropdownMenuItem(
                    onClick = {
                        navController.navigate(Screen.DownloadedScreen.route)
                    }
                ) {
                    Text(text = "Downloaded")
                }
            }
        }

        if (save.value) {
            SaveiMAGE(url, context)
            save.value = false
        }

    }

    @Composable
    fun SaveiMAGE(url: String, context: Context) {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
        ) {
            AskPermission(url, context)
        } else {
            DownloadImage(url, context)
        }
    }

    @Composable
    fun AskPermission(url: String, context: Context) {

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ) {
                showDialog(context = context)
            } else {
                ActivityCompat.requestPermissions(context,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1)
            }
        } else {
            DownloadImage(url = url, context)
        }

    }

    @SuppressLint("Range")
    @Composable
    fun DownloadImage(url: String?,context: Context) {

        download(url, context)

    }


}






