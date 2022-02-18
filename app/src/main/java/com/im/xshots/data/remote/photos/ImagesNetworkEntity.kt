package com.im.xshots.data.remote.photos


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImagesNetworkEntity(


    @Json(name = "alt")
    val alt: String,

    @Json(name = "avg_color")
    val avg_color: String,

    @Json(name = "height")
    val height: Int,

    @Json(name = "id")
    val id: Int,

    @Json(name = "liked")
    val liked: Boolean,

    @Json(name = "photographer")
    val photographer: String,

    @Json(name = "photographer_id")
    val photographer_id: Int,

    @Json(name = "photographer_url")
    val photographer_url: String,

    @Json(name = "src")
    val src: Src,

    @Json(name = "url")
    val url: String,

    @Json(name = "width")
    val width: Int,


    ) {

    @JsonClass(generateAdapter = true)
    data class Src(

        @Json(name = "landscape")
        val landscape: String,

        @Json(name = "large")
        val large: String,

        @Json(name = "large2x")
        val large2x: String,

        @Json(name = "medium")
        val medium: String,

        @Json(name = "original")
        val original: String,

        @Json(name = "portrait")
        val portrait: String,

        @Json(name = "small")
        val small: String,

        @Json(name = "tiny")
        val tiny: String,

        )


}