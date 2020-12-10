package com.udacity.asteroidradar.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.data.database.AppDatabase
import com.udacity.asteroidradar.data.database.asDomainModel
import com.udacity.asteroidradar.data.domain.Asteroid
import com.udacity.asteroidradar.data.domain.PictureOfDay
import com.udacity.asteroidradar.data.network.Network
import com.udacity.asteroidradar.data.network.asDatabaseModel
import com.udacity.asteroidradar.data.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.network.parsePictureOfDayJsonResult
import com.udacity.asteroidradar.util.getEndDateFormatted
import com.udacity.asteroidradar.util.getStartDateFormatted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AppDatabase) {

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao().getAsteroids()) {
        it.asDomainModel()
    }

    val todayAsteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao().getTodayAsteroids()) {
        it.asDomainModel()
    }

    val weekAsteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao().getWeekAsteroids()) {
        it.asDomainModel()
    }

    val pictureOfDay: LiveData<PictureOfDay> = Transformations.map(database.pictureOfTheDayDao().getPicture()) {
        it?.asDomainModel()
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            database.asteroidDao().deleteOutdatedAsteroids(getStartDateFormatted())

            val asteroidsResponse = Network.nasa.getAsteroids(
                startDate = getStartDateFormatted(),
                endDate = getEndDateFormatted(),
                apiKey = BuildConfig.NASA_API_KEY
            ).await()

            val asteroids = parseAsteroidsJsonResult(JSONObject(asteroidsResponse))

            database.asteroidDao().insertAsteroids(*asteroids.asDatabaseModel())
        }
    }

    suspend fun refreshPictureOfTheDay() {
        withContext(Dispatchers.IO) {
            val pictureOfDayResponse = Network.nasa.getPictureOfTheDay(
                date = getStartDateFormatted(),
                hd = false,
                apiKey = BuildConfig.NASA_API_KEY
            ).await()

            val pictureOfDay = parsePictureOfDayJsonResult(JSONObject(pictureOfDayResponse))

            if(pictureOfDay.pictureOfTheDay.mediaType == "image") {
                database.pictureOfTheDayDao().insertPicture(pictureOfDay.asDatabaseModel())
            }
        }
    }
}