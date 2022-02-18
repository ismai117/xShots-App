package com.im.xshots.data.remote.videos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideosNetworkResponse(

    @Json(name = "next_page")
    val next_page: String? = null,

    @Json(name = "page")
    val page: Int? = null,

    @Json(name = "per_page")
    val per_page: Int? = null,

    @Json(name = "total_results")
    val total_results: Int? = null,

    @Json(name = "url")
    val url: String? = null,

    @Json(name = "videos")
    val videos: List<VideosNetworkEntity>? = null,

)
