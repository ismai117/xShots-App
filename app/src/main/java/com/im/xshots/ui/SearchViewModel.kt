package com.im.xshots.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.im.xshots.model.images.Images
import com.im.xshots.repository.Repository
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject
constructor(
    private val repository: Repository,
) : ViewModel() {

    private val host = "PexelsdimasV1.p.rapidapi.com"
    private val key = "0c42b61f32mshd336f80f0806bb5p150e69jsn83cc84b177b5"
    private val auth = "563492ad6f91700001000001cb39055c7b394e2fbd99a86bfc2e8bb3"
    private val locale = "en-US"
    private val per_page = "15"
    private val page = "1"

    val images: MutableState<List<Images>> = mutableStateOf(listOf())

    val query = mutableStateOf("")


    fun searchImage(query: String) {

        viewModelScope.launch {
            val response = repository.getImages(
                host = host,
                key = key,
                auth = auth,
                query = query,
                locale = locale,
                per_page = per_page,
                page = page
            )

            images.value = response

        }
    }


    fun onQueryChanged(query: String){
        this.query.value = query
    }


}