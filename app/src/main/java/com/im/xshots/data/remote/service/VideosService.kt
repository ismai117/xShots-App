package com.im.xshots.data.remote.service

import com.im.xshots.data.remote.videos.VideosNetworkResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface VideosService {

    @GET("videos/search?")
    suspend fun getVideos(
        @Header("x-rapidapi-host") host: String,
        @Header("x-rapidapi-key") key: String,
        @Header("authorization") auth: String,
        @Query("query") query: String,
        @Query("per_page") per_page: String,
        @Query("page") page: String,
    ): VideosNetworkResponse

}