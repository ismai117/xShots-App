package com.im.xshots.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.im.xshots.data.local.db.XShotsDB
import com.im.xshots.data.local.images.ImagesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): XShotsDB {
        return Room.databaseBuilder(
            context.applicationContext,
            XShotsDB::class.java,
            XShotsDB.TABLE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideImagesDao(xShotsDB: XShotsDB): ImagesDao {
        return xShotsDB.getDownloadedImages()
    }


}