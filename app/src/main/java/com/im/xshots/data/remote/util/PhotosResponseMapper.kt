package com.im.xshots.data.remote.util

import com.im.xshots.data.remote.photos.ImagesNetworkEntity
import com.im.xshots.model.util.EntityMapper
import com.im.xshots.model.photos.Photos

class PhotosResponseMapper : EntityMapper<ImagesNetworkEntity, Photos> {

    override fun fromEntity(entity: ImagesNetworkEntity): Photos {
        return Photos(
            alt = entity.alt,
            avg_color = entity.avg_color,
            height = entity.height,
            id = entity.id,
            liked = entity.liked,
            photographer = entity.photographer,
            photographer_id = entity.photographer_id,
            photographer_url = entity.photographer_url,
            url = entity.url,
            width = entity.width,
            landscape = entity.src.landscape,
            large = entity.src.large,
            large2x = entity.src.large2x,
            medium = entity.src.medium,
            original = entity.src.original,
            portrait = entity.src.portrait,
            small = entity.src.small,
            tiny = entity.src.tiny,
        )
    }

    override fun toEntity(model: Photos): ImagesNetworkEntity {
        TODO("Not yet implemented")
    }

    fun mapfromEntityList(list: List<ImagesNetworkEntity>): List<Photos> {
        return list.map { fromEntity(it) }
    }

}