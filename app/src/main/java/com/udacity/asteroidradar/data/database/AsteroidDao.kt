package com.udacity.asteroidradar.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.util.getCurrentWeekEndDayDateFormatted
import com.udacity.asteroidradar.util.getStartDateFormatted

@Dao
interface AsteroidDao {

    @Query("select * from asteroid order by date(closeApproachDate) asc")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("select * from asteroid where date(closeApproachDate) == date(:today)")
    fun getTodayAsteroids(today: String = getStartDateFormatted()): LiveData<List<AsteroidEntity>>

    @Query("select * from asteroid where date(:startDate) <= date(closeApproachDate) and date(closeApproachDate) <= date(:endDate) order by date(closeApproachDate) asc")
    fun getWeekAsteroids(startDate: String = getStartDateFormatted(), endDate: String = getCurrentWeekEndDayDateFormatted()): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroids(vararg asteroids: AsteroidEntity)

    @Query("delete from asteroid where date(closeApproachDate) < date(:date)")
    fun deleteOutdatedAsteroids(date: String)
}