package com.robusta.photoweather.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.robusta.photoweather.models.domain.PhotoWeather

@Database(
    entities = [PhotoWeather::class],
    exportSchema = false,
    version = 1
)
// @TypeConverters(ClashTypeConverter::class)
abstract class PhotoWeatherDatabase :RoomDatabase() {
    abstract fun getPhotoWeatherDao() : PhotoWeatherDao
}