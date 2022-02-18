package com.im.xshots.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.im.xshots.R
import com.im.xshots.ui.components.*

import com.im.xshots.ui.theme.Fonts
import com.im.xshots.ui.util.NetworkState
import com.im.xshots.ui.util.Screen

import com.im.xshots.ui.viewmodel.PhotosViewModel
import com.im.xshots.ui.viewmodel.VideosViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@ExperimentalComposeUiApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val photosModel: PhotosViewModel by viewModels()
    private val videosModel: VideosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            photosModel.screenIsLoading.value
        }

        setContent {

            MaterialTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(photosModel, videosModel, this)
                }
            }

        }


    }

    @Composable
    fun MainScreen(searchModel: PhotosViewModel, videosModel: VideosViewModel, context: Context) {

        val navController = rememberNavController()
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        val lazyListState = rememberLazyListState()
        val bottomNavState = rememberSaveable() {
            mutableStateOf(false)
        }

        Scaffold(
            bottomBar = {
                BottomNavigation(
                    navController = navController,
                    bottomNavState = bottomNavState
                )
            },
        ) { innerPadding ->
            Box(
                modifier = Modifier.padding(innerPadding)
            ) {
                NavigationGraph(
                    navController = navController,
                    bottomNavState = bottomNavState,
                    scaffoldState = scaffoldState,
                    scope = scope,
                    photosModel = searchModel,
                    videosModel = videosModel,
                    lazyListState = lazyListState,
                    context = context)
            }
        }

    }

    @Composable
    fun NavigationGraph(
        navController: NavController,
        bottomNavState: MutableState<Boolean>,
        scaffoldState: ScaffoldState,
        scope: CoroutineScope,
        photosModel: PhotosViewModel,
        videosModel: VideosViewModel,
        lazyListState: LazyListState,
        context: Context,
    ) {

        NavHost(
            navController = navController as NavHostController,
            startDestination = Screen.PhotosScreen.route
        ) {
            composable(route = Screen.PhotosScreen.route) {
                LaunchedEffect(Unit) {
                    bottomNavState.value = true
                }
                PhotoScreen(navController, scaffoldState, scope,
                    photosModel, lazyListState, context)
            }
            composable(
                route = Screen.ImageViewScreen.route + "/{image}",
                arguments = listOf(navArgument("image") { type = NavType.StringType })
            ) { backStackEntry ->
                LaunchedEffect(Unit) {
                    bottomNavState.value = false
                }
                ImageViewScreen(
                    navController = navController,
                    scaffoldState = scaffoldState,
                    url = backStackEntry.arguments?.getString("image"),
                    context = context)
            }
            composable(
                route = Screen.VideosScreen.route
            ) {
                LaunchedEffect(Unit) {
                    bottomNavState.value = true
                }
                VideoScreen(
                    navController, scaffoldState, scope,
                    videosModel, lazyListState, context
                )
            }
            composable(
                route = Screen.VideoViewScreen.route + "/{video}",
                arguments = listOf(navArgument("video") { type = NavType.StringType })
            ) { backStackEntry ->
                LaunchedEffect(Unit) {
                    bottomNavState.value = false
                }
                VideoViewScreen(
                    scaffoldState = scaffoldState,
                    navController = navController,
                    url = backStackEntry.arguments?.getString("video"),
                    context = context
                )
            }
        }

    }

    @Composable
    fun BottomNavigation(
        navController: NavController,
        bottomNavState: MutableState<Boolean>,
    ) {
        val items = listOf(
            Screen.PhotosScreen,
            Screen.VideosScreen
        )

        AnimatedVisibility(
            visible = bottomNavState.value
        ) {
            BottomNavigation(
                backgroundColor = Color.White,
                elevation = 8.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { item ->
                    BottomNavigationItem(
                        icon = {
                            Icon(painterResource(id = item.icon),
                                contentDescription = item.title)
                        },
                        label = {
                            Text(text = item.title,
                                fontSize = 9.sp)
                        },
                        selectedContentColor = Color.Black,
                        unselectedContentColor = Color.LightGray,
                        alwaysShowLabel = true,
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {

                                navController.graph.startDestinationRoute?.let { screen_route ->
                                    popUpTo(screen_route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }

    }


    @Composable
    fun PhotoScreen(
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
                            unfocusedIndicatorColor = Color.Transparent
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

    @Composable
    fun ImageViewScreen(
        navController: NavController,
        scaffoldState: ScaffoldState,
        url: String?,
        context: Context,
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
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
                    PhotoTopBar(navController, url, context)
                },
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.Transparent,
                contentColor = Color.White
            ) {}


        }

    }

    @Composable
    fun PhotoTopBar(navController: NavController, url: String?, context: Context) {

        TopAppBar(
            title = {

            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        navController.navigate(Screen.PhotosScreen.route)
                    }
                ) {
                    Icon(Icons.Filled.ArrowBack,
                        "backIcon",
                        modifier = Modifier.size(30.dp))
                }
            },
            actions = {
                url?.let { PhotoTopBarMenu(it, context) }
            },
            backgroundColor = Color.Transparent,
            modifier = Modifier.fillMaxWidth()
        )


    }


    @Composable
    fun VideoScreen(
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
                            unfocusedIndicatorColor = Color.Transparent
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

    @Composable
    fun VideoViewScreen(
        navController: NavController,
        scaffoldState: ScaffoldState,
        url: String?,
        context: Context,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {


            Log.d("videoLink", "$url")

            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    VideoTopBar(navController, url, context)
                },
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.Transparent
            ) {
                VideoPlayer(url, context)
            }

        }

    }

    @Composable
    fun VideoTopBar(
        navController: NavController,
        url: String?,
        context: Context,
    ) {

        TopAppBar(
            title = {

            },
            navigationIcon = {
                IconButton(
                    onClick = { navController.navigate(Screen.VideosScreen.route) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = ""
                    )
                }
            },
            actions = {
                url?.let { VideoTopBarMenu(url, context) }
            },
            backgroundColor = Color.Transparent,
            modifier = Modifier.fillMaxWidth()
        )

    }

    @Composable
    fun VideoPlayer(url: String?, context: Context) {

        val uri = Uri.parse(url)

        val exoPlayer = remember(context) {
            SimpleExoPlayer.Builder(context).build().apply {
                val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, context.packageName))

                val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri)

                this.prepare(source)
            }
        }

        DisposableEffect(
            AndroidView(
                modifier =
                Modifier
                    .testTag("VideoPlayer")
                    .fillMaxSize(),
                factory = {
                    PlayerView(context).apply {
                        player = exoPlayer
                        layoutParams =
                            FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams
                                    .MATCH_PARENT,
                                ViewGroup.LayoutParams
                                    .MATCH_PARENT,
                            )
                    }
                }
            )
        ) {
            onDispose {
                exoPlayer.release()
            }
        }


    }


}






