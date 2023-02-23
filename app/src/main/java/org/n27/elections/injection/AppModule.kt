package org.n27.elections.injection

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import org.n27.elections.data.DataUtils
import org.n27.elections.data.room.Database
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

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences = app.getSharedPreferences("shared_preferences", MODE_PRIVATE)
}
