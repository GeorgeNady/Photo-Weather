package com.robusta.photoweather.di

import com.facebook.CallbackManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object FacebookModule {

    @Provides
    fun provideGoogleSignInOption() = CallbackManager.Factory.create()

}