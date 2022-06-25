package com.robusta.photoweather

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PhotoWeatherApplication : Application() {

    companion object {
        lateinit var mApplication: PhotoWeatherApplication
    }

    override fun onCreate() {
        super.onCreate()
        mApplication = this

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}