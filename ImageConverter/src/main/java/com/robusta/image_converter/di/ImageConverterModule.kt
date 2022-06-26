package com.robusta.image_converter.di

import com.robusta.image_converter.service.ImageConverterServiceImpl
import com.robusta.image_converter.repo.ImageConverterRepo
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


internal val imageConverterModule = module {

    singleOf(::ImageConverterServiceImpl) { bind<ImageConverterRepo>() }

}