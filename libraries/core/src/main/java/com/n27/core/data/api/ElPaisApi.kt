package com.n27.core.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElPaisApi @Inject constructor(private val client: OkHttpClient) {

    private val year = 2019
    private val baseUrl = "http://rsl00.epimg.net/elecciones/$year/"

    suspend fun getRegionalElection(id: String) = getResult("$baseUrl/autonomicas/$id/index.xml2")
    suspend fun getLocalAutonomy(regionId: String) = getResult("$baseUrl/municipales/$regionId/index.xml2")
    suspend fun getLocalProvince(
        regionId: String,
        provinceId: String
    ) = getResult("$baseUrl/municipales/$regionId/$provinceId.xml2")

    suspend fun getLocalElection(
        regionId: String,
        provinceId: String,
        municipalityId: String
    ) = getResult("$baseUrl/municipales/$regionId/$provinceId/$municipalityId.xml2")

    private suspend fun getResult(url: String) = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(url)
            .build()

        runCatching {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Error: $response")
                response.body?.string()
            }
        }.getOrNull()
    }
}
