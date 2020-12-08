package com.udacity.asteroidradar.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AsteroidEntity::class, PictureOfTheDayEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun asteroidDao(): AsteroidDao
    abstract fun pictureOfTheDayDao(): PictureOfTheDayDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: AppDatabase

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                if(!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, "asteroids").build()
                }
            }
            return INSTANCE
        }
    }
}