package com.im.xshots.repository

import com.im.xshots.data.remote.service.ImagesService
import com.im.xshots.data.remote.util.ImagesResponseMapper
import com.im.xshots.model.images.DownloadedImages
import com.im.xshots.model.images.Images
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository
@Inject
constructor(
    private val imagesService: ImagesService,
    private val imagesResponseMapper: ImagesResponseMapper,
) {

    suspend fun getImages(
        host: String,
        key: String,
        auth: String,
        query: String,
        locale: String,
        per_page: String,
        page: String,
    ): List<Images> {
        return imagesResponseMapper.mapfromEntityList(imagesService.getImages(host,
            key,
            auth,
            query,
            locale,
            per_page,
            page).photos)
    }



}

