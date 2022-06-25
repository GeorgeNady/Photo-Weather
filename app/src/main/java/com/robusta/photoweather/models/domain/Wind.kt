package com.robusta.photoweather.models.domain

import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("deg") val deg: Int,
    @SerializedName("speed") val speed: Double
)