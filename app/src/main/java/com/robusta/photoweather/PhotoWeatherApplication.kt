package com.robusta.photoweather

import android.app.Application
import androidx.databinding.ktx.BuildConfig
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
//         GlobalContext.startKoin { modules(imageConverterModule) }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}