package com.jorgedguezm.elections.injection

import android.app.Application
import android.content.Context
import androidx.room.Room

import com.jorgedguezm.elections.room.Database
import com.jorgedguezm.elections.room.ElectionDao
import com.jorgedguezm.elections.presentation.common.Utils
import com.jorgedguezm.elections.view.adapters.GeneralCardAdapter

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
    fun provideContext(): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideDatabase(app: Application): Database = Room
            .databaseBuilder(app, Database::class.java, "elections_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideElectionDao(database: Database): ElectionDao = database.electionDao()

    @Provides
    @Singleton
    fun provideUtils(): Utils = Utils(app)

    @Provides
    fun provideGeneralCardAdapter(utils: Utils): GeneralCardAdapter = GeneralCardAdapter(utils)
}
