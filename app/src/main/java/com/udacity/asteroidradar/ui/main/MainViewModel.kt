package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.data.database.AppDatabase.Companion.getInstance
import com.udacity.asteroidradar.data.domain.Asteroid
import com.udacity.asteroidradar.data.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import java.io.IOException

enum class AsteroidFilter {
    TODAY,
    WEEK,
    SAVED
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getInstance(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    private val _setAsteroidFilter = MutableLiveData(AsteroidFilter.SAVED)

    val asteroids = Transformations.switchMap(_setAsteroidFilter) {
        it?.let { filter ->
            when(filter) {
                AsteroidFilter.TODAY -> asteroidsRepository.todayAsteroids
                AsteroidFilter.WEEK -> asteroidsRepository.weekAsteroids
                AsteroidFilter.SAVED -> asteroidsRepository.asteroids
            }
        }
    }

    val pictureOfDay = asteroidsRepository.pictureOfDay

    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetails: LiveData<Asteroid>
        get() = _navigateToAsteroidDetails

    private val _errorOnFetchingNetworkData = MutableLiveData<Boolean>(false)
    val errorOnFetchingNetworkData: LiveData<Boolean>
        get() = _errorOnFetchingNetworkData

    fun displayNetworkErrorCompleted() {
        _errorOnFetchingNetworkData.value = false
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToAsteroidDetails.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToAsteroidDetails.value = null
    }

    fun updateAsteroidFilter(filter: AsteroidFilter) {
        _setAsteroidFilter.value = filter
    }

    init {
        refreshPictureOfTheDay()
        refreshAsteroids()
    }

    private fun refreshPictureOfTheDay() {
        viewModelScope.launch {
            try {
                asteroidsRepository.refreshPictureOfTheDay()
                _errorOnFetchingNetworkData.value = false
            } catch (networkError: IOException) {
                if(asteroids.value.isNullOrEmpty()) {
                    _errorOnFetchingNetworkData.value = true
                }
            }
        }
    }

    private fun refreshAsteroids() {
        viewModelScope.launch {
            try {
                asteroidsRepository.refreshAsteroids()
                _errorOnFetchingNetworkData.value = false
            } catch (networkError: IOException) {
                if(asteroids.value.isNullOrEmpty()) {
                    _errorOnFetchingNetworkData.value = true
                }
            }
        }
    }
}