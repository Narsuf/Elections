package com.n27.core.data.remote.api

import com.n27.core.data.local.json.JsonReader
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
    fun getRegionalElection() = runBlocking {
        enqueueResponse("regional-election-test.xml")

        val response = api.getRegionalElection("02")

        assertEquals(response?.trimIndent(), ElPaisApiResponses.regionalElection)
    }

    private suspend fun enqueueResponse(resource: String) {
        val json = JsonReader().getStringJson(resource)
        mockWebServer.enqueue(MockResponse().setBody(json))
    }

    @After
    @Throws(IOException::class)
    fun teardown() { mockWebServer.shutdown() }
}
