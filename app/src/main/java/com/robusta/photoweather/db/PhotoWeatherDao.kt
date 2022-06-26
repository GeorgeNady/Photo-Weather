package com.robusta.photoweather.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.robusta.photoweather.models.domain.PhotoWeather

@Dao
interface PhotoWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = PhotoWeather::class)
    suspend fun insertPhotoWeather(photoWeather: PhotoWeather): Long?

    @Delete
    suspend fun deletePhotoWeather(photoWeather: PhotoWeather)

    @Query("SELECT * FROM photo_weather_table ORDER BY time ASC")
    fun getPhotosWeather(): LiveData<List<PhotoWeather>>
}