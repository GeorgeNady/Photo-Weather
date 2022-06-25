package com.robusta.photoweather.network

import com.robusta.photoweather.models.response.CurrentWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface ApiService {

    @GET("/data/2.5//weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Long,
        @Query("lon") lon: Long,
        @Query("appid") apiKey: String,
        @Query("units") units: String? = "metric",
        @Query("lang") lang: String? = "ar",
    ) : Response<CurrentWeatherResponse>

}