package com.im.xshots.di


import com.im.xshots.data.remote.util.PhotosResponseMapper
import com.im.xshots.data.remote.util.VideosResponseMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InteractorsModule {


    @Singleton
    @Provides
    fun providePhotosResponseMapper(): PhotosResponseMapper {
        return PhotosResponseMapper()
    }

    @Singleton
    @Provides
    fun provideVideosResponseMapper(): VideosResponseMapper {
        return VideosResponseMapper()
    }

}