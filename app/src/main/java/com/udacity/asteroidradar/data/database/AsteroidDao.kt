package com.udacity.asteroidradar.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.data.domain.Asteroid

@Dao
interface AsteroidDao {

    @Query("select * from asteroid order by date(closeApproachDate) asc")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroids(vararg asteroids: AsteroidEntity)

    @Query("delete from asteroid where date(closeApproachDate) < date(:date)")
    fun deleteOutdatedAsteroids(date: String)
}