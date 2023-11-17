package com.n27.core.data.repositories

import com.n27.core.Constants.BAD_RESPONSE
import com.n27.core.Constants.KEY_CONGRESS
import com.n27.core.Constants.KEY_SENATE
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.Constants.REGIONAL_ELECTION_EMPTY_LIST
import com.n27.core.data.remote.api.ElDiarioApi
import com.n27.core.data.remote.api.mappers.toLiveElection
import com.n27.core.domain.live.models.LiveElections
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.test.generators.getElDiarioParties
import com.n27.test.generators.getElDiarioResult
import com.n27.test.generators.getRegions
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import kotlin.Result.Companion.success

@RunWith(RobolectricTestRunner::class)
class LiveRepositoryImplTest {

    private lateinit var repository: LiveRepositoryImpl
    private lateinit var api: ElDiarioApi

    private val ids = LocalElectionIds("01", "04", "01")

    @Before
    fun setUp() = runBlocking {
        api = mock(ElDiarioApi::class.java)

        `when`(api.getCongressParties()).thenReturn(success(getElDiarioParties()))
        `when`(api.getSenateParties()).thenReturn(success(getElDiarioParties()))
        `when`(api.getRegionalParties(anyString())).thenReturn(success(getElDiarioParties()))
        `when`(api.getLocalParties()).thenReturn(success(getElDiarioParties()))

        repository = LiveRepositoryImpl(api)
    }

    @Test
    fun getCongressElection() = runBlocking {
        val expected = success(
            getElDiarioResult(id = "")
                .toLiveElection("Generales", "España", getElDiarioParties(), KEY_CONGRESS)
        )

        `when`(api.getCongressResult()).thenReturn(success(getElDiarioResult(id = "")))

        repository.getCongressElection().collect { assertEquals(expected, it) }
    }

    @Test
    fun getSenateElection() = runBlocking {
        val expected = success(
            getElDiarioResult(id = "")
                .toLiveElection("Generales", "España", getElDiarioParties(), KEY_SENATE)
        )

        `when`(api.getSenateResult()).thenReturn(success(getElDiarioResult(id = "")))

        repository.getSenateElection().collect { assertEquals(expected, it) }
    }

    @Test
    fun getRegionalElections() = runBlocking {
        val expected = success(
            LiveElections(
                listOf(
                    getElDiarioResult(id = "01")
                        .toLiveElection("Autonómicas", "Andalucía", getElDiarioParties())
                )
            )
        )

        `when`(api.getRegionalResults()).thenReturn(listOf(getElDiarioResult(id = "01")))

        assertEquals(expected, repository.getRegionalElections(getRegions()))
    }

    @Test
    fun getRegionalElectionsEmpty(): Unit = runBlocking {
        `when`(api.getRegionalResults()).thenReturn(listOf())

       repository.getRegionalElections(getRegions()).onFailure { assertEquals(it.message, REGIONAL_ELECTION_EMPTY_LIST) }
    }

    @Test
    fun getRegionalElection() = runBlocking {
        val expected = success(
            getElDiarioResult(id = "01")
                .toLiveElection("Autonómicas", "Andalucía", getElDiarioParties())
        )

        `when`(api.getRegionalResult(anyString())).thenReturn(success(getElDiarioResult(id = "01")))

        repository.getRegionalElection("01", getRegions()).collect { assertEquals(expected, it) }
    }

    @Test
    fun getRegionalElectionWithEmptyResponse(): Unit = runBlocking {
        `when`(api.getRegionalResult(anyString())).thenReturn(null)

        runCatching { repository.getRegionalElection("01", getRegions()) }.getOrElse {
            assertEquals(it.message, BAD_RESPONSE)
        }
    }

    @Test
    fun getLocalElection() = runBlocking {
        val expected = success(
            getElDiarioResult(id = "04001")
                .toLiveElection("Municipales", "Abla", getElDiarioParties())
        )

        `when`(api.getLocalResult(ids)).thenReturn(success(getElDiarioResult()))

        repository.getLocalElection(ids, "Abla").collect { assertEquals(expected, it) }
    }

    @Test
    fun getLocalElectionWithNoConnection(): Unit = runBlocking {
        `when`(api.getLocalResult(ids)).thenReturn(null)

        runCatching { repository.getLocalElection(ids, "Abla") }.getOrElse {
            assertEquals(it.message, NO_INTERNET_CONNECTION)
        }
    }

    @Test
    fun getLocalElectionWithEmptyResponse(): Unit = runBlocking {
        `when`(api.getLocalResult(ids)).thenReturn(null)

        runCatching { repository.getLocalElection(ids, "Abla") }.getOrElse {
            assertEquals(it.message, BAD_RESPONSE)
        }
    }
}
