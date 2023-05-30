package com.n27.core.data.remote.eldiario

import com.n27.core.data.remote.eldiario.mappers.toElDiarioParties
import com.n27.core.data.remote.eldiario.mappers.toElDiarioResult
import com.n27.core.data.remote.eldiario.models.ElDiarioParty
import com.n27.core.data.remote.eldiario.models.ElDiarioResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElDiarioApi @Inject constructor(private val baseUrl: String, private val client: OkHttpClient) {

    suspend fun getRegionalResult(id: String): ElDiarioResult? =
        getResult("$baseUrl/autonomicasC$id.json")?.toElDiarioResult()

    suspend fun getRegionalParties(id: String): List<ElDiarioParty>? =
        getResult("$baseUrl/autonomicasC${id}_partidos.json")?.toElDiarioParties()


    private suspend fun getResult(url: String): String? = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            response.takeIf { it.isSuccessful }?.run { body?.string() }
        }
    }
}