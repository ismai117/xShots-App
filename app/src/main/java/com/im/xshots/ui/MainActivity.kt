package com.im.xshots.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.im.xshots.R
import com.im.xshots.model.images.Images
import com.im.xshots.ui.components.ImageCard
import com.im.xshots.ui.components.ImageList
import com.im.xshots.ui.components.ImageLoader
import com.im.xshots.ui.components.ProgressBar
import com.im.xshots.ui.theme.Fonts
import com.im.xshots.ui.util.Screen
import com.im.xshots.ui.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val searchModel: SearchViewModel by viewModels()

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            MaterialTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Navigation()
                }
            }

        }


    }

    @ExperimentalComposeUiApi
    @Composable
    fun Navigation() {

        val navController = rememberNavController()
        val scaffoldState = rememberScaffoldState()

        NavHost(
            navController = navController,
            startDestination = Screen.SearchScreen.route
        ) {
            composable(route = Screen.SearchScreen.route) {
                SearchScreen(navController = navController, searchModel)
            }
            composable(
                route = Screen.ImageScreen.route + "/{image}",
                arguments = listOf(navArgument("image") { type = NavType.StringType })
            ){ backStackEntry ->
                ImageScreen(
                    navController = navController,
                    scaffoldState = scaffoldState,
                    backStackEntry.arguments?.getString("image"))
            }
        }

    }


    @ExperimentalComposeUiApi
    @Composable
    fun SearchScreen(navController: NavController, searchModel: SearchViewModel) {

        val query = searchModel.query.value
        val images = searchModel.images.value
        val loading = searchModel.loading.value

        val coroutineScope = rememberCoroutineScope()

        Column{
            SearchItem(query = query)

            Spacer(modifier = Modifier.padding(22.dp))

            ImageList(navController = navController, images = images, context = this@MainActivity)
        }

        ProgressBar(isDisplayed = loading)

    }


    @ExperimentalComposeUiApi
    @Composable
    fun SearchItem(
        query: String,
    ){
        val softKeyboardController = LocalSoftwareKeyboardController.current

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
                modifier = Modifier.padding(top = 60.dp, start = 20.dp)
            )

            Spacer(modifier = Modifier.padding(19.dp))

            TextField(
                value = query,
                onValueChange ={
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
                    searchModel.searchImage(query = query)
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

    }



    @Composable
    fun ImageScreen(navController: NavController, scaffoldState: ScaffoldState, url: String?) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val image = url?.let { ImageLoader( uri = it, resource = R.drawable.placeholder, context = this@MainActivity).value }
            image?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    TopAppBar(
                        title = {

                        },
                        navigationIcon = {
                            IconButton(
                                onClick = { navController.navigate(Screen.SearchScreen.route) }
                            ) {
                                Icon(Icons.Filled.ArrowBack, "backIcon", modifier = Modifier.size(30.dp))
                            }
                        },
                        backgroundColor = Color.Transparent,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.Transparent,
                contentColor = Color.White
            ){}
        }

    }





}

