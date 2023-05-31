package com.n27.core.data.remote.eldiario

import com.n27.core.data.remote.eldiario.mappers.toElDiarioParties
import com.n27.core.data.remote.eldiario.mappers.toElDiarioResult
import com.n27.core.data.remote.eldiario.models.ElDiarioParty
import com.n27.core.data.remote.eldiario.models.ElDiarioResult
import com.n27.core.extensions.toStringId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElDiarioApi @Inject constructor(
    baseUrl: String,
    private val electionDate: Long,
    private val client: OkHttpClient
) {

    private val url = "$baseUrl/$electionDate"

    suspend fun getRegionalResults(): List<ElDiarioResult> {
        val elections = mutableListOf<ElDiarioResult>()

        for (i in 1..17) {
            val electionId = i.toStringId()
            getRegionalResult(electionId)?.let { elections.add(it) }
        }

        return elections
    }

    suspend fun getRegionalResult(id: String): ElDiarioResult? =
        getResult("$url/autonomicasC$id.json")?.toElDiarioResult(id, electionDate)

    suspend fun getRegionalParties(id: String): List<ElDiarioParty>? =
        getResult("$url/autonomicasC${id}_partidos.json")?.toElDiarioParties()


    private suspend fun getResult(url: String): String? = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            response.takeIf { it.isSuccessful }?.run { body?.string() }
        }
    }
}