package com.im.xshots.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.im.xshots.model.videos.Videos
import com.im.xshots.repository.MainRepository
import com.im.xshots.ui.util.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideosViewModel
@Inject
constructor(
    val repository: MainRepository,
) : ViewModel() {

    private val host = "PexelsdimasV1.p.rapidapi.com"
    private val key = "0c42b61f32mshd336f80f0806bb5p150e69jsn83cc84b177b5"
    private val auth = "563492ad6f91700001000001cb39055c7b394e2fbd99a86bfc2e8bb3"
    private val per_page = "15"
    private val page = "1"

    private val _videos: MutableState<NetworkState<List<Videos>>?> = mutableStateOf(null)
    val videos: MutableState<NetworkState<List<Videos>>?> = _videos

    val query = mutableStateOf("")

    val screenIsLoading = mutableStateOf(false)


    init {
        viewModelScope.launch {
            screenIsLoading.value = true
            delay(2000)
        }
    }

    fun searchVideos(query: String) {

        viewModelScope.launch {

            _videos.value = NetworkState.Loading()

            try {

                val response = repository.getVideos(host, key, auth, query, per_page, page)

                _videos.value = NetworkState.Success(response)

            } catch (throwable: Throwable) {

                _videos.value = NetworkState.Error(throwable)

            }

        }

    }

    fun onChangedQuery(query: String) {
        this.query.value = query
    }

}