package com.im.xshots.di

import com.im.xshots.data.remote.util.ImagesResponseMapper
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
    fun provideImagesMapper(): ImagesResponseMapper {
        return ImagesResponseMapper()
    }


}