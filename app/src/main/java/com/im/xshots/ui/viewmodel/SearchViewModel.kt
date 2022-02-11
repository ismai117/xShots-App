package com.im.xshots.ui.viewmodel

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.im.xshots.model.images.Images
import com.im.xshots.repository.Repository
import com.im.xshots.ui.util.NetworkState
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
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

    private val _images: MutableState<NetworkState<List<Images>>?> = mutableStateOf(null)
    val images: MutableState<NetworkState<List<Images>>?> = _images

    val query = mutableStateOf("")

    val screenIsLoading = mutableStateOf(false)



    init {
        viewModelScope.launch {
            screenIsLoading.value = true
            delay(3000)
        }
    }

    fun searchImage(query: String) {

        viewModelScope.launch {

            _images.value = NetworkState.Loading()

            try {

                val response = repository.getImages(
                    host = host,
                    key = key,
                    auth = auth,
                    query = query,
                    locale = locale,
                    per_page = per_page,
                    page = page
                )

                _images.value = NetworkState.Success(response)

            } catch (throwable: Throwable) {

                _images.value = NetworkState.Error(throwable)

            }

        }

    }


    fun onQueryChanged(query: String){
        this.query.value = query
    }




}