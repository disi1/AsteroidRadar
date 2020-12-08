package com.udacity.asteroidradar.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PictureOfTheDayDao {

    @Query("select * from picture_of_the_day where id = 0")
    fun getPicture(): LiveData<PictureOfTheDayEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePicture(pictureOfTheDayEntity: PictureOfTheDayEntity)
}