package com.im.xshots.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.im.xshots.model.photos.Photos
import com.im.xshots.repository.MainRepository
import com.im.xshots.ui.util.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel
@Inject
constructor(
    private val repository: MainRepository,
) : ViewModel() {

    private val host = "PexelsdimasV1.p.rapidapi.com"
    private val key = "0c42b61f32mshd336f80f0806bb5p150e69jsn83cc84b177b5"
    private val auth = "563492ad6f91700001000001cb39055c7b394e2fbd99a86bfc2e8bb3"
    private val locale = "en-US"
    private val per_page = "15"
    private val page = "1"

    private val _photos: MutableState<NetworkState<List<Photos>>?> = mutableStateOf(null)
    val photos: MutableState<NetworkState<List<Photos>>?> = _photos

    val query = mutableStateOf("")

    val screenIsLoading = mutableStateOf(false)



    init {
        viewModelScope.launch {
            screenIsLoading.value = true
            delay(3000)
        }
    }

    fun searchPhotos(query: String) {

        viewModelScope.launch {

            _photos.value = NetworkState.Loading()

            try {

                val response = repository.getPhotos(
                    host = host,
                    key = key,
                    auth = auth,
                    query = query,
                    locale = locale,
                    per_page = per_page,
                    page = page
                )

                _photos.value = NetworkState.Success(response)

            } catch (throwable: Throwable) {

                _photos.value = NetworkState.Error(throwable)

            }

        }

    }

    fun onQueryChanged(query: String){
        this.query.value = query
    }




}