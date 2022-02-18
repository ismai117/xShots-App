package com.im.xshots.di


import com.im.xshots.data.remote.service.PhotosService
import com.im.xshots.data.remote.service.VideosService
import com.squareup.moshi.Moshi

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

import retrofit2.converter.moshi.MoshiConverterFactory

import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Singleton
    @Provides
    fun provideMoshi() = Moshi.Builder().build()

    @Singleton
    @Provides
    fun provideOkHttp(): OkHttpClient {

        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    }

    @Singleton
    @Provides
    @Named("photos")
    fun providePhotosNetwork(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pexelsdimasv1.p.rapidapi.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun providePhotosService(@Named("photos") retrofit: Retrofit): PhotosService {
        return retrofit.create(PhotosService::class.java)
    }


    @Singleton
    @Provides
    @Named("videos")
    fun provideVideosNetwork(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pexelsdimasv1.p.rapidapi.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideVideosService(@Named("videos") retrofit: Retrofit): VideosService {
        return retrofit.create(VideosService::class.java)
    }


}