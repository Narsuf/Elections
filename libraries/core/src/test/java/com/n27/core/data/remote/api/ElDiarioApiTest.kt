package com.n27.core.data.remote.api

import com.n27.core.data.remote.api.mappers.toElDiarioLocalResult
import com.n27.core.data.remote.api.mappers.toElDiarioRegionalResult
import com.n27.core.data.remote.api.models.ElDiarioResult
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.core.extensions.toStringId
import com.n27.test.jsons.ElDiarioApiResponses
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
import kotlin.Result.Companion.success

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
        val election = ElDiarioApiResponses.regionalElection.toElDiarioRegionalResult("02", 2305)
        val elections = mutableListOf<ElDiarioResult>()

        for (i in 1..17) {
            mockWebServer.enqueue(MockResponse().setBody(ElDiarioApiResponses.regionalElection))
            elections.add(
                election.copy(id = i.toStringId())
            )
        }

        assertEquals(elections, api.getRegionalResults())
    }

    @Test
    fun getRegionalElection() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(ElDiarioApiResponses.regionalElection))

        val expected = success(ElDiarioApiResponses.regionalElection.toElDiarioRegionalResult("02", 2305))

        assertEquals(expected, api.getRegionalResult("02"))
    }

    @Test
    fun getLocalElection() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(ElDiarioApiResponses.localElection))

        val expected = success(ElDiarioApiResponses.localElection.toElDiarioLocalResult("04001", 2305))

        assertEquals(expected, api.getLocalResult(LocalElectionIds("01", "04", "01")))
    }

    @After
    @Throws(IOException::class)
    fun teardown() { mockWebServer.shutdown() }
}
