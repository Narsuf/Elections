package com.n27.core.presentation.injection

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.n27.core.presentation.common.PresentationUtils
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
    fun provideAnalytics() = Firebase.analytics

    @Provides
    @Singleton
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideCrashlytics() = Firebase.crashlytics
}
