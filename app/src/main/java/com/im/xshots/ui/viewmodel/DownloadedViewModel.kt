package com.im.xshots.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.im.xshots.model.images.DownloadedImages
import com.im.xshots.model.images.Images
import com.im.xshots.repository.Repository
import com.im.xshots.ui.util.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadedViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel(){


    private val _downloadImages: MutableState<NetworkState<List<DownloadedImages>>?> = mutableStateOf(null)
    val downloadImages: MutableState<NetworkState<List<DownloadedImages>>?> = _downloadImages

    init {
        viewModelScope.launch {

            _downloadImages.value = NetworkState.Loading()

            repository.getDownloadedImages().collect {

                try {
                    _downloadImages.value = NetworkState.Success(it)
                } catch (throwable: Throwable){
                    _downloadImages.value = NetworkState.Error(throwable)
                }


            }

        }
    }


}

