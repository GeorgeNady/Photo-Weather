package com.robusta.photoweather.models.domain

import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("country") val country: String,
    @SerializedName("id") val id: Int,
    @SerializedName("message") val message: Double,
    @SerializedName("sunrise") val sunrise: Int,
    @SerializedName("sunset") val sunset: Int,
    @SerializedName("type") val type: Int
)