package com.udacity.asteroidradar.data.network

import com.squareup.moshi.Json
import com.udacity.asteroidradar.data.database.AsteroidEntity
import com.udacity.asteroidradar.data.database.PictureOfTheDayEntity

data class NetworkAsteroidContainer(val asteroids: List<NetworkAsteroid>)
data class NetworkPictureOfTheDayContainer(val pictureOfTheDay: NetworkPictureOfTheDay)

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

data class NetworkPictureOfTheDay(
    @Json(name = "media_type") val mediaType: String,
    val title: String,
    val url: String
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

fun NetworkPictureOfTheDayContainer.asDatabaseModel(): PictureOfTheDayEntity {
    return PictureOfTheDayEntity(
        mediaType = pictureOfTheDay.mediaType,
        title = pictureOfTheDay.title,
        url = pictureOfTheDay.url
    )
}