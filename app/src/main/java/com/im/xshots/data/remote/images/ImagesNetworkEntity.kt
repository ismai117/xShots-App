package com.im.xshots.data.remote.images

import com.google.gson.annotations.SerializedName


data class ImagesNetworkEntity(

    val photos: List<Photo> = listOf(),

    ) {


    data class Photo(

        @SerializedName(value = "alt")
        val alt: String,

        @SerializedName(value = "avg_color")
        val avg_color: String,

        @SerializedName(value = "height")
        val height: Int,

        @SerializedName(value = "id")
        val id: Int,

        @SerializedName(value = "liked")
        val liked: Boolean,

        @SerializedName(value = "photographer")
        val photographer: String,

        @SerializedName(value = "photographer_id")
        val photographer_id: Int,

        @SerializedName(value = "photographer_url")
        val photographer_url: String,

        @SerializedName(value = "src")
        val src: Src,

        @SerializedName(value = "url")
        val url: String,

        @SerializedName(value = "width")
        val width: Int,

        )


    data class Src(

        @SerializedName(value = "landscape")
        val landscape: String,

        @SerializedName(value = "large")
        val large: String,

        @SerializedName(value = "large2x")
        val large2x: String,

        @SerializedName(value = "medium")
        val medium: String,

        @SerializedName(value = "original")
        val original: String,

        @SerializedName(value = "portrait")
        val portrait: String,

        @SerializedName(value = "small")
        val small: String,

        @SerializedName(value = "tiny")
        val tiny: String,

        )

}
