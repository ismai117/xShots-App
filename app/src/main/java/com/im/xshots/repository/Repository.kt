package com.im.xshots.repository

import android.util.Log
import com.im.xshots.data.local.db.XShotsDB
import com.im.xshots.data.local.images.ImagesDao
import com.im.xshots.data.local.util.ImagesCacheMapper
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
    private val imagesDao: ImagesDao,
    private val imagesResponseMapper: ImagesResponseMapper,
    private val imagesCacheMapper: ImagesCacheMapper
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

    fun getDownloadedImages(): Flow<List<DownloadedImages>> {
        return imagesCacheMapper.mapfromEntityFlowList(imagesDao.getDownloadedImages())
    }

    suspend fun insertDownloadedImages(downloadedImages: DownloadedImages){
        imagesDao.insert(imagesCacheMapper.toEntity(downloadedImages))
    }

}

