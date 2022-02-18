package com.im.xshots.di

import com.im.xshots.data.remote.service.PhotosService
import com.im.xshots.data.remote.service.VideosService
import com.im.xshots.data.remote.util.PhotosResponseMapper
import com.im.xshots.data.remote.util.VideosResponseMapper
import com.im.xshots.repository.MainRepository
import com.im.xshots.repository.MainRepository_Impl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Singleton
    @Provides
    fun provideMainRepository(
        photosService: PhotosService,
        videosService: VideosService,
        photosResponseMapper: PhotosResponseMapper,
        videosResponseMapper: VideosResponseMapper
    ): MainRepository {
        return MainRepository_Impl(
            photosService,
            videosService,
            photosResponseMapper,
            videosResponseMapper
        )
    }


}