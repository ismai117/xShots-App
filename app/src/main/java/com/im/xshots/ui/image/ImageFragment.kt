package com.im.xshots.ui.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.im.xshots.R
import com.im.xshots.ui.components.ImageLoader

class ImageFragment : Fragment() {

    private var imageUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val url = ImageFragmentArgs.fromBundle(it).images
            url?.let {
                imageUri = url.portrait
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        return ComposeView(requireContext()).apply {

            setContent {

                val scaffoldState = rememberScaffoldState()
                val image = remember { mutableStateOf("") }

                MaterialTheme() {
                    Surface(color = MaterialTheme.colors.background) {
                        MainScreen(scaffoldState, image)
                    }
                }
            }

        }

    }


    @Composable
    fun MainScreen(scaffoldState: ScaffoldState, image: MutableState<String>) {

        Box {

            imageUri?.let {
                image.value = it
            }

            val itemImage = ImageLoader(uri = image.value,
                resource = R.drawable.placeholder,
                context = requireContext()).value
            itemImage?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
            Scaffold(
                backgroundColor = Color.Transparent,
                scaffoldState = scaffoldState,
                topBar = { AppBar() }
            ) {}
        }

    }


    @Composable
    fun AppBar() {
        TopAppBar(
            title = {
                Text(text = "")
            },
            navigationIcon = {
                IconButton(
                    onClick = { findNavController().popBackStack() },
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "",
                        tint = Color.White,
                    )
                }
            },
            backgroundColor = Color.Transparent,
        )
    }


}