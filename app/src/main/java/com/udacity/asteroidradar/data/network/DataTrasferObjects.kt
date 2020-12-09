package com.udacity.asteroidradar.data.network

import com.udacity.asteroidradar.data.database.AsteroidEntity

data class NetworkAsteroidContainer(val asteroids: List<NetworkAsteroid>)

data class NetworkAsteroid (
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

fun NetworkAsteroidContainer.asDatabaseModel(): Array<AsteroidEntity> {
    return asteroids.map {
        AsteroidEntity(
            it.id,
            it.codename,
            it.closeApproachDate,
            it.absoluteMagnitude,
            it.estimatedDiameter,
            it.relativeVelocity,
            it.distanceFromEarth,
            it.isPotentiallyHazardous
        )
    }.toTypedArray()
}