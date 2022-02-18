package com.im.xshots.data.remote.util

import com.im.xshots.data.remote.videos.VideosNetworkEntity
import com.im.xshots.data.remote.videos.VideosNetworkResponse
import com.im.xshots.model.util.EntityMapper
import com.im.xshots.model.videos.Videos
import com.squareup.moshi.Json

class VideosResponseMapper : EntityMapper<VideosNetworkEntity, Videos> {

    override fun fromEntity(entity: VideosNetworkEntity): Videos {
        return Videos(
            avg_color = entity.avg_color,
            duration = entity.duration,
            full_res = entity.full_res,
            height = entity.height,
            id = entity.id,
            image = entity.image,
            tags = entity.tags,
            url = entity.url,
            width = entity.width,
            video_files = mapFromVideoFileList(entity.video_files!!),
            video_pictures = mapFromVideoPicturesList(entity.video_pictures!!),
            userId = entity.user?.userId,
            name = entity.user?.name,
            userUrlrl = entity.user?.userUrl,

            )
    }

    override fun toEntity(model: Videos): VideosNetworkEntity {
        TODO("Not yet implemented")
    }

    fun mapfromEntityList(entityList: List<VideosNetworkEntity>): List<Videos> {
        return entityList.map { fromEntity(it) }
    }

    fun mapFromVideoFile(video: VideosNetworkEntity.VideoFile): Videos.VideoFile {
        return Videos.VideoFile(

            file_type = video.file_type,
            videoFilEhHight = video.videoFileHeight,
            videoFileId = video.videoFileId,
            link = video.link,
            quality = video.quality,
            videoFileWidth = video.videoFileWidth,

            )
    }

    fun mapFromVideoFileList(video: List<VideosNetworkEntity.VideoFile>): List<Videos.VideoFile> {
        return video.map { mapFromVideoFile(it) }
    }


    fun mapFromVideoPictures(video: VideosNetworkEntity.VideoPicture): Videos.VideoPicture {
        return Videos.VideoPicture(

            videoId = video.videoId,
            nr = video.nr,
            picture = video.picture,

            )
    }

    fun mapFromVideoPicturesList(video: List<VideosNetworkEntity.VideoPicture>): List<Videos.VideoPicture> {
        return video.map { mapFromVideoPictures(it) }
    }

}