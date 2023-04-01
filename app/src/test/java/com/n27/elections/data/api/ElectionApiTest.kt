package com.n27.elections.data.api

import com.n27.core.data.local.json.JsonReader
import com.n27.elections.data.api.models.ApiResponse
import com.n27.test.generators.getElection
import com.n27.test.generators.getElections
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException

class ElectionApiTest {

    private lateinit var apiInterface: ElectionApi
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun init() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        apiInterface = Retrofit.Builder().client(OkHttpClient.Builder().build())
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build()))
            .build().create(ElectionApi::class.java)
    }

    @Test
    fun getApiElections() = runBlocking {
        enqueueResponse("elections-test.json")

        val response = apiInterface.getElections()

        assertEquals(response, ApiResponse(getElections()))
    }

    @Test
    fun getApiElection() = runBlocking {
        val apiResponse = getElection()

        enqueueResponse("election-test.json")

        val response = apiInterface.getElection(1)

        assertEquals(response, apiResponse)
    }

    private suspend fun enqueueResponse(resource: String) {
        val json = JsonReader().getStringJson(resource)
        val mockResponse = MockResponse()
        mockWebServer.enqueue(mockResponse.setBody(json))
    }

    @After
    @Throws(IOException::class)
    fun teardown() { mockWebServer.shutdown() }
}
