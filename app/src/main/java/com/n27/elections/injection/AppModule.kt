package com.n27.elections.injection

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.n27.core.data.common.DataUtils
import com.n27.core.data.local.room.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

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
    fun provideDataUtils() = DataUtils(app)

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences = app.getSharedPreferences("shared_preferences", MODE_PRIVATE)
}
