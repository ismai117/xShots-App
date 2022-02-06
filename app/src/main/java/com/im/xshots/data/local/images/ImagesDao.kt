package com.im.xshots.data.local.images

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface ImagesDao {


    @Query("SELECT * FROM images_table")
    fun getDownloadedImages(): Flow<List<ImagesCacheEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(imagesCacheEntity: ImagesCacheEntity)

    @Query("DELETE FROM images_table")
    fun deleteeteDownloadedImages()


}