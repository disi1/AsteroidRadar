package com.udacity.asteroidradar.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.data.domain.Asteroid

@Entity(tableName = "asteroid")
data class AsteroidEntity constructor(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

@Entity(tableName = "picture_of_the_day")
data class PictureOfTheDayEntity constructor(
    @PrimaryKey
    val id: Int = 0,
    val mediaType: String,
    val title: String,
    val url: String
)

fun List<AsteroidEntity>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            it.id,
            it.codename,
            it.closeApproachDate,
            it.absoluteMagnitude,
            it.estimatedDiameter,
            it.relativeVelocity,
            it.distanceFromEarth,
            it.isPotentiallyHazardous
        )
    }
}