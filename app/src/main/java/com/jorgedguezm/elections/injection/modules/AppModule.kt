package com.jorgedguezm.elections.injection.modules

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room

import com.jorgedguezm.elections.data.source.local.Database
import com.jorgedguezm.elections.data.source.local.ElectionsDao
import com.jorgedguezm.elections.data.source.local.PartiesDao
import com.jorgedguezm.elections.data.source.local.ResultsDao
import com.jorgedguezm.elections.utils.Utils
import com.jorgedguezm.elections.ui.ElectionsViewModelFactory

import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module
class AppModule(val app: Application) {

    /*companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Change the table name to the correct one
                database.execSQL("ALTER TABLE election RENAME TO elections")
            }
        }
    }*/

    @Provides
    @Singleton
    fun provideApplication(): Application = app

    @Provides
    @Singleton
    fun provideElectionsDatabase(app: Application): Database = Room.databaseBuilder(app,
            Database::class.java, "elections_db")
            //.addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideElectionsDao(database: Database): ElectionsDao = database.electionsDao()

    @Provides
    @Singleton
    fun providePartiesDao(database: Database): PartiesDao = database.partiesDao()

    @Provides
    @Singleton
    fun provideResultsDao(database: Database): ResultsDao = database.resultsDao()

    @Provides
    fun provideElectionsViewModelFactory(
            factory: ElectionsViewModelFactory): ViewModelProvider.Factory = factory

    @Provides
    @Singleton
    fun provideUtils(): Utils = Utils(app)
}