package com.im.xshots.data.local.util

import com.im.xshots.data.local.images.ImagesCacheEntity
import com.im.xshots.model.images.DownloadedImages
import com.im.xshots.model.util.EntityMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ImagesCacheMapper : EntityMapper<ImagesCacheEntity, DownloadedImages> {


    override fun fromEntity(entity: ImagesCacheEntity): DownloadedImages {
        return DownloadedImages(
            url = entity.images
        )
    }

    override fun toEntity(model: DownloadedImages): ImagesCacheEntity {
        return ImagesCacheEntity(
            id = 0,
            images = model.url
        )
    }

    fun mapfromEntityList(list: List<ImagesCacheEntity>): List<DownloadedImages> {
        return list.map { fromEntity(it) }
    }

    fun mapfromEntityFlowList(list: Flow<List<ImagesCacheEntity>>): Flow<List<DownloadedImages>> {
        return list.map { mapfromEntityList(it) }
    }



}