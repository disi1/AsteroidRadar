package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.data.database.AppDatabase.Companion.getInstance
import com.udacity.asteroidradar.data.domain.Asteroid
import com.udacity.asteroidradar.data.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getInstance(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    val asteroids = asteroidsRepository.asteroids

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

    init {
        refreshAsteroids()
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