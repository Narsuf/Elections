package com.n27.core.presentation.injection

import com.google.firebase.database.FirebaseDatabase
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
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance()
}
