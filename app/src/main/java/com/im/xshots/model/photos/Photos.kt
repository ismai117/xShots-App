package com.im.xshots.model.photos

import com.im.xshots.data.remote.photos.ImagesNetworkEntity
import java.io.Serializable

data class Photos(

    val id: Int,
    val alt: String,
    val avg_color: String,
    val height: Int,
    val liked: Boolean,
    val photographer: String,
    val photographer_id: Int,
    val photographer_url: String,
    val url: String,
    val width: Int,
    val landscape: String,
    val large: String,
    val large2x: String,
    val medium: String,
    val original: String,
    val portrait: String,
    val small: String,
    val tiny: String,

) : Serializable
