package com.n27.core.data.remote.api

import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.DataUtils
import com.n27.core.data.remote.api.mappers.toElDiarioLocalResult
import com.n27.core.data.remote.api.mappers.toElDiarioParties
import com.n27.core.data.remote.api.mappers.toElDiarioRegionalResult
import com.n27.core.data.remote.api.mappers.toElDiarioResult
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
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import java.io.IOException
import kotlin.Result.Companion.success

@RunWith(RobolectricTestRunner::class)
class ElDiarioApiTest {

    private lateinit var api: ElDiarioApi
    private lateinit var mockWebServer: MockWebServer
    private lateinit var dataUtils: DataUtils

    @Before
    fun init() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        dataUtils = mock(DataUtils::class.java)
        `when`(dataUtils.isConnectedToInternet()).thenReturn(true)

        api = ElDiarioApi(
            baseUrl = mockWebServer.url("/").toUrl().toString(),
            electionDate = 2305,
            client = OkHttpClient.Builder().build(),
            utils = dataUtils
        )
    }

    @Test
    fun getParties() = runBlocking {
        for (i in 0..3) mockWebServer.enqueue(MockResponse().setBody(ElDiarioApiResponses.regionalParties))

        val expected = success(ElDiarioApiResponses.regionalParties.toElDiarioParties())

        assertEquals(expected, api.getCongressParties())
        assertEquals(expected, api.getSenateParties())
        assertEquals(expected, api.getRegionalParties(""))
        assertEquals(expected, api.getLocalParties())
    }

    @Test
    fun getCongressElection() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(ElDiarioApiResponses.congressElection))

        val expected = success(ElDiarioApiResponses.congressElection.toElDiarioResult(2305, 350))

        assertEquals(expected, api.getCongressResult())
    }

    @Test
    fun `getCongressElection with no internet`(): Unit = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(ElDiarioApiResponses.congressElection))

        `when`(dataUtils.isConnectedToInternet()).thenReturn(false)

        api.getCongressResult().let { result ->
            result.onFailure { assertEquals(it.message, NO_INTERNET_CONNECTION) }
            assertNull(result.getOrNull())
        }
    }

    @Test
    fun getSenateElection() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(ElDiarioApiResponses.senateElection))

        val expected = success(ElDiarioApiResponses.senateElection.toElDiarioResult(2305, 208))

        assertEquals(expected, api.getSenateResult())
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
