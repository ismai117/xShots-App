package com.im.xshots.data.local.images

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images_table")
class ImagesCacheEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
     val images: String? = null,

){

}