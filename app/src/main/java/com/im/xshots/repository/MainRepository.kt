package com.im.xshots.repository

import com.im.xshots.data.remote.service.PhotosService
import com.im.xshots.data.remote.util.PhotosResponseMapper
import com.im.xshots.model.photos.Photos
import com.im.xshots.model.videos.Videos
import javax.inject.Inject


interface MainRepository {

    suspend fun getPhotos(
        host: String,
        key: String,
        auth: String,
        query: String,
        locale: String,
        per_page: String,
        page: String,
    ): List<Photos>

    suspend fun getVideos(
        host: String,
        key: String,
        auth: String,
        query: String,
        per_page: String,
        page: String,
    ): List<Videos>

}


