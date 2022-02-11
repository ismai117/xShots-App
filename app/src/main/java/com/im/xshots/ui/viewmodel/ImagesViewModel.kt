package com.im.xshots.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.im.xshots.model.images.DownloadedImages
import com.im.xshots.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel
@Inject
constructor(
    val repository: Repository
) : ViewModel() {



    fun insertDownloadedImages(downloadedImages: DownloadedImages){
        viewModelScope.launch {
            repository.insertDownloadedImages(downloadedImages = downloadedImages)
        }
    }




}