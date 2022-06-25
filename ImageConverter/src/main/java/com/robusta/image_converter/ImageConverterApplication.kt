package com.robusta.image_converter

import android.app.Application
import com.robusta.image_converter.di.imageConverterModule
import org.koin.core.context.GlobalContext.startKoin

internal class ImageConverterApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin { modules(imageConverterModule) }
    }
}