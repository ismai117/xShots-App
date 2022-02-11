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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
import com.im.xshots.ui.viewmodel.DownloadedViewModel
import com.im.xshots.ui.viewmodel.ImagesViewModel

import com.im.xshots.ui.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val searchModel: SearchViewModel by viewModels()
    private val imagesViewModel: ImagesViewModel by viewModels()
    private val downloadedViewModel: DownloadedViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            searchModel.screenIsLoading.value
        }

        setContent {

            MaterialTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Navigation(searchModel, imagesViewModel, downloadedViewModel)
                }
            }

        }


    }


    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun Navigation(
        searchModel: SearchViewModel,
        imagesViewModel: ImagesViewModel,
        downloadedViewModel: DownloadedViewModel,
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
                    searchModel, lazyListState)
            }
            composable(
                route = Screen.ImageScreen.route + "/{image}",
                arguments = listOf(navArgument("image") { type = NavType.StringType })
            ) { backStackEntry ->
                ImageScreen(
                    navController = navController,
                    scaffoldState = scaffoldState,
                    backStackEntry.arguments?.getString("image"),
                    imagesViewModel)
            }
            composable(route = Screen.DownloadedScreen.route) {
                DownloadedScreen(navController, scaffoldState, scope,
                    downloadedViewModel, this@MainActivity)
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
                                context = this@MainActivity)
                        }

                    }

                    is NetworkState.Error -> {

                        Toast.makeText(this@MainActivity,
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
        imagesViewModel: ImagesViewModel,
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
                    context = this@MainActivity).value
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
                    TopBar(navController = navController, url, imagesViewModel)
                },
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.Transparent,
                contentColor = Color.White
            ) {}
        }

        }

    }


    @Composable
    fun TopBar(navController: NavController, url: String?, imagesViewModel: ImagesViewModel) {

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
                url?.let { ShowMenu(navController, it, imagesViewModel) }
            },
            backgroundColor = Color.Transparent,
            modifier = Modifier.fillMaxWidth()
        )


    }

    private @Composable
    fun ShowMenu(
        navController: NavController,
        url: String,
        imagesViewModel: ImagesViewModel
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
            SaveiMAGE(url, imagesViewModel)
            save.value = false
        }

    }

    @Composable
    fun SaveiMAGE(url: String, imagesViewModel: ImagesViewModel) {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
        ) {
            AskPermission(url, imagesViewModel)
        } else {
            DownloadImage(url, imagesViewModel)
        }
    }

    @Composable
    fun AskPermission(url: String, imagesViewModel: ImagesViewModel) {
        if (ContextCompat.checkSelfPermission(this@MainActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ) {
                ShowDialog(this)
            } else {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1)
            }
        } else {
            DownloadImage(url = url, imagesViewModel)
        }
    }

    @SuppressLint("Range")
    @Composable
    fun DownloadImage(url: String?, imagesViewModel: ImagesViewModel) {

        val scope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()
        val lastMessage = remember { mutableStateOf("") }

        val dir = File(Environment.DIRECTORY_PICTURES)

        if(!dir.exists()){
            dir.mkdirs()
        }

        val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val uri = Uri.parse(url)

        val request = DownloadManager.Request(uri).apply {

            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
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


                var isDownloading = true

                while (isDownloading){

                    val cursor: Cursor = downloadManager.query(query)

                    cursor.moveToFirst()

                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL){
                        isDownloading = false
                        val insertDownloadedImage = DownloadedImages(url)
                        imagesViewModel.insertDownloadedImages(insertDownloadedImage)
                    }

                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

                    val message = url?.let { statusMessages(status, dir, it) }

                    if (message != lastMessage.value){
                        this.runOnUiThread{

                            Log.d("download_status", "${message}")

                            Toast.makeText(this, "$message",Toast.LENGTH_LONG).show()

                            lastMessage.value = message ?: ""
                        }
                    }

                    cursor.close()
                }

        }).start()

        }


    }

     fun statusMessages(status: Int, dir: File, url: String): String {

        val msg = mutableStateOf("")

        when(status){

            DownloadManager.STATUS_SUCCESSFUL -> {
                msg.value = "Download status is successful"
            }

            DownloadManager.STATUS_RUNNING -> {
                msg.value = "Download status is running"
            }

            DownloadManager.STATUS_PAUSED -> {
                msg.value = "Download statis is paused"
            }

            DownloadManager.STATUS_PENDING -> {
                msg.value = "Download status is pending"
            }

            DownloadManager.STATUS_FAILED -> {
                msg.value = "Download status is failed"
            }

            else -> {
               msg.value = "There's nothing to download"
            }

        }


        return msg.value

    }


    @Composable
    fun ShowDialog(context: Context) {
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


    @Composable
    fun DownloadedScreen(
        navController: NavHostController,
        scaffoldState: ScaffoldState,
        scope: CoroutineScope,
        downloadedViewModel: DownloadedViewModel,
        context: Context
    ) {

        val downloadedImages = downloadedViewModel.downloadImages.value


        when (downloadedImages) {


            is NetworkState.Success -> {

                downloadedImages.data?.let {

                    DownloadScreenLayout(navController = navController,
                        scaffoldState = scaffoldState,
                        images = it, context)

                }

            }

            is NetworkState.Error -> {

                Toast.makeText(context,
                    "${downloadedImages.error?.message}",
                    Toast.LENGTH_LONG).show()

            }

            is NetworkState.Loading -> {

                ProgressBar(isDisplayed = true)

            }

            else -> {}
        }


    }


    @Composable
    fun DownloadScreenLayout(
        navController: NavHostController,
        scaffoldState: ScaffoldState,
        images: List<DownloadedImages>,
        context: Context
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                DownloadedTopBar(navController)
            },
            content = {
                DownloadedImagesGrid(images = images, context)
            }
        )
    }

    @Composable
    fun DownloadedTopBar(
        navController: NavController,
    ) {
        TopAppBar(
            title = {

            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        navController.navigate(Screen.ImageScreen.route + "/{image}")
                    }
                ) {
                    Icon(Icons.Filled.ArrowBack,
                        "backIcon",
                        modifier = Modifier.size(30.dp))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.White
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun DownloadedImagesGrid(images: List<DownloadedImages>, context: Context) {

        LazyVerticalGrid(
            cells = GridCells.Fixed(3),
            modifier = Modifier.padding(top = 1.dp, bottom = 1.dp)
        ) {
            items(images) { item ->
                Card(
                    modifier = Modifier.padding(start = 1.dp, end = 1.dp),
                    backgroundColor = Color.LightGray
                ) {
                    val image = item.url?.let {
                        ImageLoader(uri = it,
                            resource = 0,
                            context = context).value
                    }
                    image?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "",
                            modifier = Modifier
                                .height(130.dp)
                                .width(150.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                }
            }

        }


}
