package com.n27.elections.data

import com.n27.elections.data.utils.getApiResponse
import com.n27.elections.data.utils.getElection
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.nio.charset.StandardCharsets

class ElectionApiTest {

    private lateinit var apiInterface: ElectionApi
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun init() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        apiInterface = Retrofit.Builder().client(OkHttpClient.Builder().build())
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory
                .create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build()))
            .build().create(ElectionApi::class.java)
    }

    @Test
    fun getApiElections() = runBlocking {
        enqueueResponse("elections-test.json")

        val response = apiInterface.getElections()

        assertEquals(response, getApiResponse())
    }

    @Test
    fun getApiElection() = runBlocking {
        val apiResponse = getElection()

        enqueueResponse("election-test.json")

        val response = apiInterface.getElection(1)

        assertEquals(response, apiResponse)
    }

    private fun enqueueResponse(resource: String) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(resource)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockWebServer.enqueue(mockResponse.setBody(source.readString(StandardCharsets.UTF_8)))
    }

    @After
    @Throws(IOException::class)
    fun teardown() {
        mockWebServer.shutdown()
    }
}
