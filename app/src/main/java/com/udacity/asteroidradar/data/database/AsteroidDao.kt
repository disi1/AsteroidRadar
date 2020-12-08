package com.udacity.asteroidradar.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.udacity.asteroidradar.data.domain.Asteroid

@Dao
interface AsteroidDao {

    @Query("select * from asteroid")
    fun getAsteroids():LiveData<List<AsteroidEntity>>
}