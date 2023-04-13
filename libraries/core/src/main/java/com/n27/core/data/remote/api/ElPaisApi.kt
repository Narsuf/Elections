package com.n27.core.data.remote.api

import com.n27.core.data.remote.api.models.LocalElectionIds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElPaisApi @Inject constructor(baseUrl: String, private val client: OkHttpClient) {

    private val year = 2019 // TODO: Set year with feature flag.
    private val baseUrl = "$baseUrl/$year"

    suspend fun getRegionalElection(id: String) = getResultOrNull("$baseUrl/autonomicas/$id/index.xml2")

    /*suspend fun getLocalAutonomy(regionId: String) = getResultOrNull("$baseUrl/municipales/$regionId/index.xml2")

    suspend fun getLocalProvince(
        regionId: String,
        provinceId: String
    ) = getResultOrNull("$baseUrl/municipales/$regionId/$provinceId.xml2")*/

    suspend fun getLocalElection(
        ids: LocalElectionIds
    ) = getResult("$baseUrl/municipales/${ids.region}/${ids.province}/${ids.municipality}.xml2")

    private suspend fun getResult(url: String) = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Error: $response")
            response.body?.string()
        }
    }

    private suspend fun getResultOrNull(url: String) = runCatching { getResult(url) }.getOrNull()

}
