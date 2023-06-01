package com.n27.core.data.remote.api

import com.n27.core.data.local.json.JsonReader
import com.n27.core.data.remote.api.mappers.toElDiarioLocalResult
import com.n27.core.data.remote.api.mappers.toElDiarioRegionalResult
import com.n27.core.data.remote.api.models.ElDiarioResult
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.core.extensions.toStringId
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class ElDiarioApiTest {

    private lateinit var api: ElDiarioApi
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun init() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        api = ElDiarioApi(
            baseUrl = mockWebServer.url("/").toUrl().toString(),
            electionDate = 2305,
            client = OkHttpClient.Builder().build()
        )
    }

    @Test
    fun getRegionalElections() = runBlocking {
        val election = JsonReader().getStringJson("regional-election-test.json")
            .toElDiarioRegionalResult("02", 2305)
        val elections = mutableListOf<ElDiarioResult>()

        for (i in 1..17) {
            enqueueResponse("regional-election-test.json")
            elections.add(
                election.copy(id = i.toStringId())
            )
        }

        assertEquals(elections, api.getRegionalResults())
    }

    @Test
    fun getRegionalElection() = runBlocking {
        enqueueResponse("regional-election-test.json")

        val response = api.getRegionalResult("02")

        assertEquals(response, ElPaisApiResponses.regionalElection.toElDiarioRegionalResult("02", 2305))
    }

    @Test
    fun getLocalElection() = runBlocking {
        enqueueResponse("local-election-test.json")

        val response = api.getLocalResult(LocalElectionIds("01", "04", "01"))

        assertEquals(response, ElPaisApiResponses.localElection.toElDiarioLocalResult("04001", 2305))
    }

    private suspend fun enqueueResponse(resource: String) {
        val json = JsonReader().getStringJson(resource)
        mockWebServer.enqueue(MockResponse().setBody(json))
    }

    @After
    @Throws(IOException::class)
    fun teardown() { mockWebServer.shutdown() }
}
