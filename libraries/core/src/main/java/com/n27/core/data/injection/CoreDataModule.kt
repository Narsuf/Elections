package com.n27.core.data.injection

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
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
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig() = FirebaseRemoteConfig.getInstance()

    @Provides
    @Singleton
    fun providesBaseUrl(
        remoteConfig: FirebaseRemoteConfig
    ) = "${BuildConfig.EL_PAIS_API_URL}/${remoteConfig.getLong("ELECTION_YEAR")}"
}
