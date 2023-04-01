package com.n27.core.data.injection

import com.n27.core.BuildConfig
import com.n27.core.data.local.json.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class CoreDataModule {

    @Provides
    @Singleton
    fun providesJsonReader() = JsonReader()

    @Provides
    @Singleton
    fun providesMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun providesOkHttpClient() = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun providesBaseUrl() = BuildConfig.EL_PAIS_API_URL
}
