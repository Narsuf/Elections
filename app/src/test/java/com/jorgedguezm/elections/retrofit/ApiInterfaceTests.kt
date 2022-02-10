package com.jorgedguezm.elections.retrofit

import com.jorgedguezm.elections.api.ApiInterface
import com.jorgedguezm.elections.models.ApiResponse
import com.jorgedguezm.elections.models.Election
import com.jorgedguezm.elections.models.Party
import com.jorgedguezm.elections.models.Results

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking

import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source

import org.junit.Before
import org.junit.Test

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.nio.charset.StandardCharsets

class ApiInterfaceTests {

    private lateinit var apiInterface: ApiInterface
    private lateinit var mockWebServer: MockWebServer

    companion object {
        // Expected response from test.json
        val apiResponse = ApiResponse(mutableListOf(
            Election(3, "Generales", "2015", "España", "Congreso",
                350, 100.0F, 25349824, 9280429,
                187766, 226994, mutableListOf(
                    Results(123, 7215530, Party("PP", "006EC7"))
                ))
        ))
    }

    @Before
    fun init() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        apiInterface = Retrofit.Builder().client(OkHttpClient.Builder().build())
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory
                .create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build()))
            .build().create(ApiInterface::class.java)
    }

    @Test
    fun getElections() = runBlocking {
        enqueueResponse()

        val response = apiInterface.getElections("España")

        assertEquals(response, apiResponse)
    }

    private fun enqueueResponse() {
        val inputStream = javaClass.classLoader!!.getResourceAsStream("test.json")
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockWebServer.enqueue(mockResponse.setBody(source.readString(StandardCharsets.UTF_8)))
    }
}