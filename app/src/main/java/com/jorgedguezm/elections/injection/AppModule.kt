package com.jorgedguezm.elections.injection

import android.app.Application
import androidx.room.Room
import com.jorgedguezm.elections.data.DataUtils
import com.jorgedguezm.elections.data.room.Database
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
    fun provideDataUtils() = DataUtils(app)
}
