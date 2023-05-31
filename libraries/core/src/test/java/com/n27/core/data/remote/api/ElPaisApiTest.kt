package com.n27.core.data.remote.api

import com.n27.core.data.local.json.JsonReader
import com.n27.core.data.remote.api.mappers.toElectionXml
import com.n27.core.data.remote.api.models.ElectionXml
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.extensions.toStringId
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ElPaisApiTest {

    private lateinit var api: ElPaisApi
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun init() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        api = ElPaisApi(
            client = OkHttpClient.Builder().build(),
            baseUrl = mockWebServer.url("/").toUrl().toString()
        )
    }

    @Test
    fun getRegionalElections() = runBlocking {
        val election = JsonReader().getStringJson("regional-election-test.xml").toElectionXml()
        val elections = mutableListOf<ElectionXml>()

        for (i in 1..17) {
            enqueueResponse("regional-election-test.xml")
            elections.add(
                election.copy(id = i.toStringId())
            )
        }

        assertEquals(elections, api.getRegionalElections())
    }

    @Test
    fun getRegionalElection() = runBlocking {
        enqueueResponse("regional-election-test.xml")

        val response = api.getRegionalElection("02")

        assertEquals(response, ElPaisApiResponses.regionalElection.toElectionXml())
    }

    @Test
    fun getLocalElection() = runBlocking {
        enqueueResponse("local-election-test.xml")

        val response = api.getLocalElection(LocalElectionIds("01", "04", "01"))

        assertEquals(response, ElPaisApiResponses.localElection.toElectionXml())
    }

    private suspend fun enqueueResponse(resource: String) {
        val json = JsonReader().getStringJson(resource)
        mockWebServer.enqueue(MockResponse().setBody(json))
    }

    @After
    @Throws(IOException::class)
    fun teardown() { mockWebServer.shutdown() }
}
