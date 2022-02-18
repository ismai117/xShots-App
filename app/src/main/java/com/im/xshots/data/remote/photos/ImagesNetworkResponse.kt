package com.im.xshots.data.remote.photos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImagesNetworkResponse(

    @Json(name = "page")
    val page: Int,

    @Json(name = "per_page")
    val per_page: Int,

    @Json(name = "photos")
    val photos: List<ImagesNetworkEntity> = listOf(),

    )