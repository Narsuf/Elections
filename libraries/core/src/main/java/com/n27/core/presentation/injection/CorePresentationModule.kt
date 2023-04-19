package com.n27.core.presentation.injection

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.n27.core.presentation.PresentationUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CorePresentationModule {

    @Provides
    @Singleton
    fun providePresentationUtils() = PresentationUtils()

    @Provides
    @Singleton
    fun provideFirebaseCrashlytics() = Firebase.crashlytics
}
