package com.robusta.photoweather.models.domain

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.BLOB
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "photo_weather_table"
)
data class PhotoWeather(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id",) val id: Int? = 0,
    @ColumnInfo(name = "time") val time: Long? = Date().time,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "temp") val temp: Double?,
    @ColumnInfo(name = "feels_like") val feelsLike: Double?,
    @ColumnInfo(name = "humidity") val humidity: Int?,
    @ColumnInfo(name = "wind_speed") val windSpeed: Double?,
    @ColumnInfo(name = "wind_deg") val windDeg: Int?,
    @ColumnInfo(name = "image", typeAffinity = BLOB) val image: ByteArray?,
    @ColumnInfo(name = "thumbnail", typeAffinity = BLOB) val thumbnail: ByteArray?
) {

    constructor(
        name: String?,
        temp: Double?,
        feelsLike: Double?,
        humidity: Int?,
        windSpeed: Double?,
        windDeg: Int?,
        image: ByteArray?,
        thumbnail: ByteArray?
    ) : this(
        null,
        Date().time,
        name,
        temp,
        feelsLike,
        humidity,
        windSpeed,
        windDeg,
        image,
        thumbnail
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PhotoWeather

        if (id != other.id) return false
        if (time != other.time) return false
        if (name != other.name) return false
        if (temp != other.temp) return false
        if (feelsLike != other.feelsLike) return false
        if (humidity != other.humidity) return false
        if (windSpeed != other.windSpeed) return false
        if (windDeg != other.windDeg) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (time?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (temp?.hashCode() ?: 0)
        result = 31 * result + (feelsLike?.hashCode() ?: 0)
        result = 31 * result + (humidity ?: 0)
        result = 31 * result + (windSpeed?.hashCode() ?: 0)
        result = 31 * result + (windDeg ?: 0)
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return """
            name: $name
            temp: $temp
            feelsLike: $feelsLike
            humidity: $humidity
            windSpeed: $windSpeed
            windDeg: $windDeg
            image: $image
            thumbnail: $thumbnail
        """.trimIndent()
    }

}
