package com.n27.core.injection

import android.app.Application
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CoreModule {

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig() = FirebaseRemoteConfig.getInstance()
}