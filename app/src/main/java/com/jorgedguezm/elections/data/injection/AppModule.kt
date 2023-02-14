package com.jorgedguezm.elections.data.injection

import android.app.Application
import androidx.room.Room
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.jorgedguezm.elections.data.utils.DataUtils
import com.jorgedguezm.elections.data.room.Database
import com.jorgedguezm.elections.presentation.common.PresentationUtils
import com.jorgedguezm.elections.presentation.main.adapters.GeneralCardAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: Application) {

    @Provides
    @Singleton
    fun provideApplication() = app

    @Provides
    @Singleton
    fun provideDatabase(app: Application) = Room
            .databaseBuilder(app, Database::class.java, "elections_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideElectionDao(database: Database) = database.electionDao()

    @Provides
    @Singleton
    fun providePresentationUtils() = PresentationUtils()

    @Provides
    @Singleton
    fun provideDataUtils() = DataUtils(app)

    @Provides
    fun provideGeneralCardAdapter(utils: PresentationUtils) = GeneralCardAdapter(utils)

    @Provides
    @Singleton
    fun provideFirebaseAnalytics() = Firebase.analytics

    @Provides
    @Singleton
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance()
}
