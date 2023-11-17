package com.n27.test.applications

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDexApplication
import androidx.room.Room
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.n27.core.data.DataUtils
import com.n27.core.data.local.json.JsonReader
import com.n27.core.data.local.room.Database
import com.n27.core.data.remote.api.ElDiarioApi
import com.n27.core.data.repositories.LiveRepositoryImpl
import com.n27.core.data.repositories.RegionRepositoryImpl
import com.n27.core.domain.LiveUseCase
import com.n27.core.injection.CoreComponent
import com.n27.core.injection.CoreComponentProvider
import com.n27.core.presentation.PresentationUtils
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

class CoreTestApplication : MultiDexApplication(), CoreComponentProvider {

    override fun provideCoreComponent(): CoreTestApplicationComponent = DaggerCoreTestApplicationComponent
        .builder()
        .coreFakeModule(CoreFakeModule(this))
        .build()
}

@Singleton
@Component(modules = [CoreFakeModule::class])
interface CoreTestApplicationComponent : CoreComponent

@Module
class CoreFakeModule(val app: Application) {

    @Provides
    @Singleton
    fun provideApplication() = app

    @Provides
    @Singleton
    fun provideContext(): Context = app.applicationContext

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
    fun providesJsonReader() = JsonReader()

    @Provides
    @Singleton
    fun providesMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun providePresentationUtils(): PresentationUtils = PresentationUtils()

    @Provides
    @Singleton
    fun provideBaseUrl() = "http://127.0.0.1:8080"

    @Provides
    @Singleton
    fun providesElectionDate() = 2305L

    @Provides
    @Singleton
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics? = null

    @Provides
    @Singleton
    fun provideLiveUseCase(baseUrl: String, date: Long, client: OkHttpClient, jsonReader: JsonReader, moshi: Moshi) = LiveUseCase(
        LiveRepositoryImpl(ElDiarioApi(baseUrl, date, client, DataUtils(app))),
        RegionRepositoryImpl(jsonReader, moshi)
    )
}
