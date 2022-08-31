package com.jorgedguezm.elections.data

import com.jorgedguezm.elections.data.models.ApiResponse
import com.jorgedguezm.elections.data.models.Election
import com.jorgedguezm.elections.data.models.Party
import com.jorgedguezm.elections.data.models.Results
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
import org.mockito.ArgumentMatchers.anyString
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.nio.charset.StandardCharsets

class ElectionApiTest {

    private lateinit var apiInterface: ElectionApi
    private lateinit var mockWebServer: MockWebServer

    companion object {
        val expectedElection = Election(3, "Generales", "2015", "España",
            "Congreso", 350, 100.0F, 25349824,
            9280429, 187766, 226994, mutableListOf(
                Results(123, 7215530, Party("PP", "006EC7"))
            )
        )

        val expectedApiResponse = ApiResponse(listOf(expectedElection))
    }

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
    fun getElections() = runBlocking {
        enqueueResponse("elections-test.json")

        val response = apiInterface.getElections("España")

        assertEquals(response, expectedApiResponse)
    }

    @Test
    fun getElection() = runBlocking {
        val apiResponse = ApiResponse(expectedElection)

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
