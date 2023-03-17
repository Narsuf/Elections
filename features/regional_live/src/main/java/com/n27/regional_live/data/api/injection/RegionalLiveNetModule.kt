package com.n27.regional_live.data.api.injection

import com.n27.regional_live.data.api.ElPaisApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit.Builder
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Singleton

@Module
class RegionalLiveNetModule {

    @Provides
    @Singleton
    fun providesElPaisApi(okHttpClient: OkHttpClient): ElPaisApi {
        return Builder().client(okHttpClient).baseUrl("http://rsl00.epimg.net/elecciones/")
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
            .create(ElPaisApi::class.java)
    }
}
