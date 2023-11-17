package com.n27.test.applications

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDexApplication
import androidx.room.Room
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.n27.core.data.common.DataUtils
import com.n27.core.data.local.json.JsonReader
import com.n27.core.data.local.json.RegionRepositoryImpl
import com.n27.core.data.local.room.Database
import com.n27.core.data.remote.api.ElDiarioApi
import com.n27.core.data.remote.api.LiveRepositoryImpl
import com.n27.core.domain.LiveUseCase
import com.n27.core.presentation.PresentationUtils
import com.n27.core.presentation.injection.DetailComponent
import com.n27.core.presentation.injection.DetailComponentProvider
import com.n27.regional_live.presentation.injection.RegionalLiveComponent
import com.n27.regional_live.presentation.injection.RegionalLiveComponentProvider
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

class TestApplication : MultiDexApplication(), DetailComponentProvider, RegionalLiveComponentProvider {

    override fun provideDetailComponent(): TestApplicationComponent = DaggerTestApplicationComponent.builder()
        .fakeModule(FakeModule(this))
        .build()

    override fun provideRegionalLiveComponent(): TestApplicationComponent = DaggerTestApplicationComponent.builder()
        .fakeModule(FakeModule(this))
        .build()
}

@Singleton
@Component(modules = [FakeModule::class])
interface TestApplicationComponent : DetailComponent, RegionalLiveComponent

@Module
class FakeModule(val app: Application) {

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
    fun provideLiveUseCase(baseUrl: String, client: OkHttpClient, jsonReader: JsonReader, moshi: Moshi) = LiveUseCase(
        LiveRepositoryImpl(ElDiarioApi(baseUrl, 1, client, DataUtils(app))),
        RegionRepositoryImpl(jsonReader, moshi)
    )
}
