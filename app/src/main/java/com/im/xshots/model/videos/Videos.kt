package com.im.xshots.model.videos

import com.im.xshots.data.remote.videos.VideosNetworkEntity
import com.squareup.moshi.Json
import java.io.Serializable

data class Videos(

    val avg_color: Any? = null,
    val duration: Int? = null,
    val full_res: Any? = null,
    val height: Int? = null,
    val id: Int? = null,
    val image: String? = null,
    val tags: List<Any>? = null,
    val url: String? = null,
    val width: Int? = null,

    val video_files: List<VideoFile>,

    val video_pictures: List<VideoPicture>,

    val userId: Int? = null,
    val name: String? = null,
    val userUrlrl: String? = null,

    val videoId: Int? = null,
    val nr: Int? = null,
    val picture: String? = null,


    ) : Serializable {


    data class VideoFile(

        val file_type: String? = null,
        val videoFilEhHight: Int? = null,
        val videoFileId: Int? = null,
        val link: String? = null,
        val quality: String? = null,
        val videoFileWidth: Int? = null,

        )

    data class VideoPicture(

        val videoId: Int? = null,
        val nr: Int? = null,
        val picture: String? = null,

    )


}