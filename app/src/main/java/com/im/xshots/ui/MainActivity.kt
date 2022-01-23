package com.im.xshots.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
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
import com.im.xshots.ui.components.ImageCard
import com.im.xshots.ui.components.ImageLoader
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
                ImageScreen(navController = navController, backStackEntry.arguments?.getString("image"))
            }
        }

    }


    @ExperimentalComposeUiApi
    @Composable
    fun SearchScreen(navController: NavController, searchModel: SearchViewModel) {

        val query = searchModel.query.value
        val images = searchModel.images.value
        val softKeyboardController = LocalSoftwareKeyboardController.current

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {

            item {

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

                    Spacer(modifier = Modifier.padding(20.dp))

                    TextField(
                        value = query,
                        onValueChange = { queryValue ->
                            searchModel.onQueryChanged(query = queryValue)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, start = 20.dp, end = 20.dp),
                        shape = RoundedCornerShape(50.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(onSearch = {
                            searchModel.searchImage(query = query)
                            softKeyboardController?.hide()
                        }),
                        leadingIcon = {
                            Icons.Filled.Search
                        },
                        label = {
                            Text(text = "Search")
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                    )

                }

            }

            item { Spacer(modifier = Modifier.padding(8.dp)) }

            itemsIndexed(
                items = images
            ) { index, image ->
                ImageCard(
                    images = image,
                    onClick = {
                        val encodedUrl = URLEncoder.encode("${image.portrait}", StandardCharsets.UTF_8.toString())
                        navController.navigate(Screen.ImageScreen.route + "/$encodedUrl")
                    },
                    context = this@MainActivity
                )
            }

        }

    }

    @Composable
    fun ImageScreen(navController: NavController, url: String?) {

        Column(
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
        }

    }

}

