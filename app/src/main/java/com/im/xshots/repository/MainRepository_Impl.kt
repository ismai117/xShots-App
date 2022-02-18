package com.im.xshots.repository

import com.im.xshots.data.remote.service.PhotosService
import com.im.xshots.data.remote.service.VideosService
import com.im.xshots.data.remote.util.PhotosResponseMapper
import com.im.xshots.data.remote.util.VideosResponseMapper
import com.im.xshots.model.photos.Photos
import com.im.xshots.model.videos.Videos
import javax.inject.Inject

class MainRepository_Impl
@Inject
constructor(
    private val photoService: PhotosService,
    private val videosService: VideosService,
    private val photoResponseMapper: PhotosResponseMapper,
    private val videosResponseMapper: VideosResponseMapper
) : MainRepository {

    override suspend fun getPhotos(
        host: String,
        key: String,
        auth: String,
        query: String,
        locale: String,
        per_page: String,
        page: String,
    ): List<Photos> {
        val response = photoService.getImages(host, key, auth, query, locale, per_page, page).photos
        return photoResponseMapper.mapfromEntityList(response)
    }

    override suspend fun getVideos(
        host: String,
        key: String,
        auth: String,
        query: String,
        per_page: String,
        page: String,
    ): List<Videos> {
        val response = videosService.getVideos(host, key, auth, query, per_page, page).videos
        return videosResponseMapper.mapfromEntityList(response!!)
    }


}