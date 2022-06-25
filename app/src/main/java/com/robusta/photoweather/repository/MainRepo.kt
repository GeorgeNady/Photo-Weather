package com.robusta.photoweather.repository

import com.robusta.photoweather.network.ApiService
import com.robusta.photoweather.network.BaseDataSource
import javax.inject.Inject

class MainRepo @Inject constructor(
    private val api: ApiService,
    // private val dao: PhotoWeatherDao
) : BaseDataSource() {

    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String?,
        lang: String?
    ) = safeApiCall { api.getCurrentWeather(lat, lon, apiKey, units, lang) }

    // suspend fun insertPhotoWeather(photoWeather: PhotoWeather) = dao.insertPhotoWeather(photoWeather)

    // suspend fun deletePhotoWeather(photoWeather: PhotoWeather) = dao.deletePhotoWeather(photoWeather)

    // fun getPhotosWeather() = dao.getPhotosWeather()

}