/*
package com.robusta.photoweather.models.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity(tableName = "photo_weather_table")
public class PhotoWeatherEntity {
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") Long id;
    @ColumnInfo(name = "time") Long time = new Date().getTime();
    @ColumnInfo(name = "name") String name;
    @ColumnInfo(name = "temp") double temp;
    @ColumnInfo(name = "feels_like") double feelsLike;
    @ColumnInfo(name = "humidity") int humidity;
    @ColumnInfo(name = "wind_speed") double windSpeed;
    @ColumnInfo(name = "wind_deg") int windDeg;
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB) byte[] image;
}
*/
