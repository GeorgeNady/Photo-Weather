package com.robusta.photoweather.repository

import com.robusta.photoweather.network.ApiService
import com.robusta.photoweather.network.BaseDataSource
import javax.inject.Inject

class MainRepo @Inject constructor(
    private val apiService: ApiService
) : BaseDataSource() {

    suspend fun getCurrentWeather(
        lon: Long,
        lat: Long,
        apiKey: String,
        units: String?,
        lang: String?
    ) = safeApiCall { apiService.getCurrentWeather(lon, lat, apiKey, units, lang) }

}