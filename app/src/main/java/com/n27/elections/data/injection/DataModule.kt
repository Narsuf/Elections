package com.n27.elections.data.injection

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.n27.core.data.common.DataUtils
import com.n27.core.data.local.room.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

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
    fun provideDataUtils(app: Application) = DataUtils(app)

    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences = app
        .getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
}