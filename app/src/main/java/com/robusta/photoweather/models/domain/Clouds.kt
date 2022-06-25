package com.robusta.photoweather.models.domain

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all") val all: Int
)