package com.jorgedguezm.elections.injection.modules

import android.app.Application

import com.jorgedguezm.elections.utils.Utils

import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module
class AppModule(val app: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application = app

    @Provides
    @Singleton
    fun provideUtils(): Utils = Utils(app)
}