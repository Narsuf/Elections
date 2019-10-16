package com.jorgedguezm.elections.injection.modules

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room

import com.jorgedguezm.elections.data.source.local.Database
import com.jorgedguezm.elections.data.source.local.ElectionsDao
import com.jorgedguezm.elections.Utils
import com.jorgedguezm.elections.ui.ElectionsViewModelFactory
import com.jorgedguezm.elections.ui.adapters.GeneralCardAdapter
import com.jorgedguezm.elections.ui.detail.DetailActivityViewModelFactory

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
    fun provideElectionsViewModelFactory(
            factory: ElectionsViewModelFactory): ViewModelProvider.Factory = factory

    @Provides
    fun provideDetailActivityViewModelFactory(
            factory: DetailActivityViewModelFactory): ViewModelProvider.Factory = factory

    @Provides
    @Singleton
    fun provideUtils(): Utils = Utils(app)

    @Provides
    fun provideGeneralCardAdapter(utils: Utils): GeneralCardAdapter = GeneralCardAdapter(app, utils)
}