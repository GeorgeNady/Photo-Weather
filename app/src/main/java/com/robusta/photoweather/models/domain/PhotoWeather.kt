package com.robusta.photoweather.models.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import lombok.Builder
import java.util.*

@Entity(
    tableName = "photo_weather_table"
)
@Builder
data class PhotoWeather(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "time") val time: Long? = Date().time,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "temp") val temp: Double?,
    @ColumnInfo(name = "feels_like") val feelsLike: Double?,
    @ColumnInfo(name = "humidity") val humidity: Int?,
    @ColumnInfo(name = "wind_speed") val windSpeed: Double?,
    @ColumnInfo(name = "wind_deg") val windDeg: Int?,
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB) val image: ByteArray?,
    @ColumnInfo(name = "thumbnail", typeAffinity = ColumnInfo.BLOB) val thumbnail: ByteArray?
) {




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

}
