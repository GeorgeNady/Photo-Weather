package com.robusta.photoweather.di

import android.content.Context
import androidx.room.Room
import com.robusta.photoweather.db.PhotoWeatherDatabase
import com.robusta.photoweather.utilities.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    @Named("db_name")
    fun provideDatabaseName() = DATABASE_NAME

    @Provides
    @Singleton
    fun providePhotoWeatherDao(
        db: PhotoWeatherDatabase
    ) = db.getPhotoWeatherDao()

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext app: Context,
        @Named("db_name") dbName: String
    ) = Room.databaseBuilder(
        app,
        PhotoWeatherDatabase::class.java,
        dbName
    ).build()

}