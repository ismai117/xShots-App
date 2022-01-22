package com.im.xshots.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

import androidx.compose.ui.platform.ComposeView

import androidx.compose.ui.platform.LocalSoftwareKeyboardController

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.im.xshots.model.images.Images
import com.im.xshots.ui.components.ImageCard
import com.im.xshots.ui.theme.Fonts
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {


    private val searchModol: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @ExperimentalComposeUiApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {

            setContent {

                MaterialTheme {
                    Surface(color = MaterialTheme.colors.background) {
                        MainScreen(searchModol)
                    }
                }

            }


        }
    }

    @ExperimentalComposeUiApi
    @Composable
    fun MainScreen(searchModol: SearchViewModel) {

        val images = searchModol.images.value
        val query = searchModol.query.value


        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            ImagesList(images = images, query)

        }


    }

    @ExperimentalComposeUiApi
    @Composable
    fun ImagesList(images: List<Images>, query: String) {

        val softKeyboardController = LocalSoftwareKeyboardController.current

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {

            item {
                Text(
                    text = "Discover Photo's",
                    style = TextStyle(
                        fontSize = 27.sp,
                        fontFamily = Fonts,
                    ),
                    modifier = Modifier.padding(top = 44.dp, start = 22.dp)
                )
                Spacer(modifier = Modifier.padding(8.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 22.dp,
                                end = 22.dp
                            ),
                        shape = RoundedCornerShape(50.dp),
                        value = query,
                        onValueChange = {
                            searchModol.onQueryChanged(it)
                        },
                        label = {
                            Text(text = "Search")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search
                        ),
                        leadingIcon = {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "")
                        },
                        keyboardActions = KeyboardActions(onSearch = {
                            searchModol.searchImage(query)
                            softKeyboardController?.hide()
                        })
                    )
                }
                Spacer(modifier = Modifier.padding(12.dp))
            }

            itemsIndexed(
                items = images
            ) { index, images ->
                ImageCard(images = images, {
                    val action = SearchFragmentDirections.actionSearchFragmentToImageFragment()
                    action.images = images
                    findNavController().navigate(action)
                }, requireContext())
            }

            item {
                Spacer(modifier = Modifier.padding(bottom = 15.dp))
            }

        }

    }


}