package com.jorgedguezm.elections.data.injection

import android.app.Application
import androidx.room.Room
import com.jorgedguezm.elections.data.DataUtils

import com.jorgedguezm.elections.data.room.Database
import com.jorgedguezm.elections.data.room.ElectionDao
import com.jorgedguezm.elections.presentation.common.PresentationUtils
import com.jorgedguezm.elections.presentation.main.adapters.GeneralCardAdapter

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
    fun provideDatabase(app: Application): Database = Room
            .databaseBuilder(app, Database::class.java, "elections_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideElectionDao(database: Database): ElectionDao = database.electionDao()

    @Provides
    @Singleton
    fun providePresentationUtils(): PresentationUtils = PresentationUtils()

    @Provides
    @Singleton
    fun provideDataUtils(): DataUtils = DataUtils(app)

    @Provides
    fun provideGeneralCardAdapter(utils: PresentationUtils): GeneralCardAdapter = GeneralCardAdapter(utils)
}
