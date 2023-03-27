package com.n27.elections.data.api.injection

import com.n27.elections.BuildConfig
import com.n27.elections.data.api.ElectionApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit.Builder
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class AppNetModule {

    @Provides
    @Singleton
    fun providesElectionApi(okHttpClient: OkHttpClient, moshi: Moshi): ElectionApi {
        return Builder().client(okHttpClient).baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(ElectionApi::class.java)
    }
}
