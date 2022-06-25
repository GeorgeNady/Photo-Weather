package com.robusta.photoweather.models.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.IDN
import java.util.*

@Entity(
    tableName = "photo_weather_table"
)
data class PhotoWeather(
    @PrimaryKey(autoGenerate = false)
    val idn: Long,
    val time: Long = Date().time,
    val name: String,
    val temp: Double,
    val feels_like: Double,
    val humidity: Int,
    val wind_speed: Double,
    val wind_deg: Int
)
