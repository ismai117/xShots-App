package com.im.xshots.data.remote.service

import com.im.xshots.data.remote.photos.ImagesNetworkEntity
import com.im.xshots.data.remote.photos.ImagesNetworkResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PhotosService {

    @GET("v1/search?")
    suspend fun getImages(
        @Header("x-rapidapi-host") host: String,
        @Header("x-rapidapi-key") key: String,
        @Header("authorization") auth: String,
        @Query("query") query: String,
        @Query("locale") locale: String,
        @Query("per_page") per_page: String,
        @Query("page") page: String,
    ) : ImagesNetworkResponse

}