package com.jorgedguezm.elections.presentation.injection

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.jorgedguezm.elections.presentation.common.PresentationUtils
import com.jorgedguezm.elections.presentation.main.adapters.GeneralCardAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PresentationModule {

    @Provides
    @Singleton
    fun providePresentationUtils() = PresentationUtils()

    @Provides
    fun provideGeneralCardAdapter(utils: PresentationUtils) = GeneralCardAdapter(utils)

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