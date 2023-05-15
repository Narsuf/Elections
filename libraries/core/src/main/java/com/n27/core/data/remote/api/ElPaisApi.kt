package com.n27.core.data.remote.api

import com.n27.core.data.remote.api.mappers.toElectionXml
import com.n27.core.data.remote.api.models.ElectionXml
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.extensions.toStringId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElPaisApi @Inject constructor(private val baseUrl: String, private val client: OkHttpClient) {

    suspend fun getRegionalElection(id: String) = getRegionalElectionResult(id)?.toElectionXml()

    private suspend fun getRegionalElectionResult(id: String): String? =
        getResult("$baseUrl/autonomicas/$id/index.xml2")

    suspend fun getLocalElection(ids: LocalElectionIds): ElectionXml? =
        getResult("$baseUrl/municipales/${ids.region}/${ids.province}/${ids.municipality}.xml2")
            ?.toElectionXml()

    private suspend fun getResult(url: String): String? = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            response.takeIf { it.isSuccessful }?.run { body?.string() }
        }
    }

    suspend fun getRegionalElections(): List<ElectionXml> {
        val elections = mutableListOf<ElectionXml>()

        for (i in 1..17) {
            val electionId = i.toStringId()

            getRegionalElectionResult(electionId)?.let {
                elections.add(it.toElectionXml(electionId))
            }
        }

        return elections
    }
}
