package com.im.xshots.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.im.xshots.data.local.images.ImagesCacheEntity
import com.im.xshots.data.local.images.ImagesDao


@Database(
    entities = [ImagesCacheEntity::class],
    version = 4,
    exportSchema = false
)
abstract class XShotsDB : RoomDatabase(){

    abstract fun getDownloadedImages(): ImagesDao

    companion object{
       val TABLE_NAME = "images_table"
    }
}