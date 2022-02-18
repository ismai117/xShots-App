package com.im.xshots.data.remote.videos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class VideosNetworkEntity(

    @Json(name = "avg_color")
    val avg_color: Any? = null,

    @Json(name = "duration")
    val duration: Int? = null,

    @Json(name = "full_res")
    val full_res: Any? = null,

    @Json(name = "height")
    val height: Int? = null,

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "image")
    val image: String? = null,

    @Json(name = "tags")
    val tags: List<Any>? = null,

    @Json(name = "url")
    val url: String? = null,

    @Json(name = "user")
    val user: User? = null,

    @Json(name = "video_files")
    val video_files: List<VideoFile>? = null,

    @Json(name = "video_pictures")
    val video_pictures: List<VideoPicture>? = null,

    @Json(name = "width")
    val width: Int? = null,

) {

    @JsonClass(generateAdapter = true)
    data class VideoFile(

        @Json(name = "file_type")
        val file_type: String? = null,

        @Json(name = "height")
        val videoFileHeight: Int? = null,

        @Json(name = "id")
        val videoFileId: Int? = null,

        @Json(name = "link")
        val link: String? = null,

        @Json(name = "quality")
        val quality: String? = null,

        @Json(name = "width")
        val videoFileWidth: Int? = null,

    )

    @JsonClass(generateAdapter = true)
    data class User(

        @Json(name = "id")
        val userId: Int? = null,

        @Json(name = "name")
        val name: String? = null,

        @Json(name = "url")
        val userUrl: String? = null,

    )

    @JsonClass(generateAdapter = true)
    data class VideoPicture(

        @Json(name = "id")
        val videoId: Int? = null,

        @Json(name = "nr")
        val nr: Int? = null,

        @Json(name = "picture")
        val picture: String? = null,

    )

}



