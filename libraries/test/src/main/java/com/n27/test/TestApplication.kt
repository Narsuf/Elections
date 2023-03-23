package com.n27.test

import android.app.Application
import androidx.multidex.MultiDexApplication
import androidx.room.Room
import com.n27.core.data.room.Database
import com.n27.core.presentation.common.PresentationUtils
import com.n27.core.presentation.injection.DetailComponent
import com.n27.core.presentation.injection.DetailComponentProvider
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

class TestApplication : MultiDexApplication(), DetailComponentProvider {

    override fun provideDetailComponent(): TestApplicationComponent = DaggerTestApplicationComponent.builder()
        .fakeModule(FakeModule(this))
        .build()
}

@Singleton
@Component(modules = [FakeModule::class])
interface TestApplicationComponent : DetailComponent

@Module
class FakeModule(val app: Application) {

    @Provides
    @Singleton
    fun providePresentationUtils(): PresentationUtils = PresentationUtils()

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

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
    fun provideApplication() = app
}
