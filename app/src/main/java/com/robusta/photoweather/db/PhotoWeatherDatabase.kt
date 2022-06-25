package com.robusta.photoweather.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.robusta.photoweather.models.domain.PhotoWeather
import com.robusta.photoweather.utilities.Constants.DATABASE_NAME

@Database(
    entities = [PhotoWeather::class],
    exportSchema = false,
    version = 1
)
// @TypeConverters(ClashTypeConverter::class)
abstract class PhotoWeatherDatabase :RoomDatabase() {

    abstract fun getPhotoWeatherDao() : PhotoWeatherDao

    companion object {
        private var DATABASE_INSTANCE : PhotoWeatherDatabase? = null

        fun getPhotoWeatherDatabase(context:Context): PhotoWeatherDatabase {
            if (DATABASE_INSTANCE == null) {
                DATABASE_INSTANCE = Room.databaseBuilder<PhotoWeatherDatabase>(
                    context.applicationContext, PhotoWeatherDatabase::class.java, DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return DATABASE_INSTANCE!!
        }
    }
}