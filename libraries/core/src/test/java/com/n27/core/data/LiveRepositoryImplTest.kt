package com.n27.core.data

import com.n27.core.Constants.BAD_RESPONSE
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.common.DataUtils
import com.n27.core.data.local.json.JsonReader
import com.n27.core.data.local.room.ElectionDao
import com.n27.core.data.local.room.mappers.toParties
import com.n27.core.data.remote.api.ElPaisApi
import com.n27.core.data.remote.api.mappers.toElectionXml
import com.n27.core.data.remote.api.mappers.toLiveElection
import com.n27.core.data.remote.api.mappers.toLiveElections
import com.n27.core.data.remote.api.models.ElectionXml
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.extensions.toStringId
import com.n27.test.generators.getPartyRaw
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
    private lateinit var api: ElPaisApi
    private lateinit var dao: ElectionDao
    private lateinit var dataUtils: DataUtils

    private val ids = LocalElectionIds("", "", "")

    @Before
    fun setUp() = runBlocking {
        api = mock(ElPaisApi::class.java)
        dao = mock(ElectionDao::class.java)
        dataUtils = mock(DataUtils::class.java)

        `when`(dao.getParties()).thenReturn(listOf())
        `when`(dataUtils.isConnectedToInternet()).thenReturn(true)

        repository = LiveRepositoryImpl(api, dao, dataUtils)
    }

    @Test
    fun getRegionalElections() = runBlocking {
        val election = JsonReader().getStringJson("regional-election-test.xml").toElectionXml()
        val parties = listOf(getPartyRaw(), getPartyRaw(name = "PSOE"))
        val elections = mutableListOf<ElectionXml>()

        for (i in 1..17) {
            elections.add(
                election.copy(id = i.toStringId())
            )
        }

        val expected = success(elections.toLiveElections(parties.toParties()))

        `when`(dao.getParties()).thenReturn(parties)
        `when`(api.getRegionalElections()).thenReturn(elections)

        assertEquals(expected, repository.getRegionalElections())
    }

    @Test
    fun getRegionalElectionsEmpty(): Unit = runBlocking {
        `when`(api.getRegionalElections()).thenReturn(listOf())

       repository.getRegionalElections().apply {
           assertTrue(isFailure)
           onFailure { assertEquals(it.message, BAD_RESPONSE) }
       }
    }

    @Test
    fun getRegionalElection() = runBlocking {
        val response = JsonReader().getStringJson("regional-election-test.xml").toElectionXml()
        val parties = listOf(getPartyRaw(), getPartyRaw(name = "PSOE"))
        val expected = success(response.toLiveElection(parties.toParties()))

        `when`(dao.getParties()).thenReturn(parties)
        `when`(api.getRegionalElection(anyString())).thenReturn(response)

        assertEquals(expected, repository.getRegionalElection("01"))
    }

    @Test
    fun getRegionalElectionWithNoConnection(): Unit = runBlocking {
        `when`(dataUtils.isConnectedToInternet()).thenReturn(false)
        `when`(api.getRegionalElection(anyString())).thenReturn(null)

        runCatching { repository.getRegionalElection("01") }.getOrElse {
            assertEquals(it.message, NO_INTERNET_CONNECTION)
        }
    }

    @Test
    fun getRegionalElectionWithEmptyResponse(): Unit = runBlocking {
        `when`(api.getRegionalElection(anyString())).thenReturn(null)

        runCatching { repository.getRegionalElection("01") }.getOrElse {
            assertEquals(it.message, BAD_RESPONSE)
        }
    }

    @Test
    fun getLocalElection() = runBlocking {
        val response = JsonReader().getStringJson("local-election-test.xml").toElectionXml()
        val parties = listOf(getPartyRaw(), getPartyRaw(name = "PSOE-A"))
        val expected = success(response.toLiveElection(parties.toParties()))

        `when`(dao.getParties()).thenReturn(parties)
        `when`(api.getLocalElection(ids)).thenReturn(response)

        assertEquals(expected, repository.getLocalElection(ids))
    }

    @Test
    fun getLocalElectionWithNoConnection(): Unit = runBlocking {
        `when`(dataUtils.isConnectedToInternet()).thenReturn(false)
        `when`(api.getLocalElection(ids)).thenReturn(null)

        runCatching { repository.getLocalElection(ids) }.getOrElse {
            assertEquals(it.message, NO_INTERNET_CONNECTION)
        }
    }

    @Test
    fun getLocalElectionWithEmptyResponse(): Unit = runBlocking {
        `when`(api.getLocalElection(ids)).thenReturn(null)

        runCatching { repository.getLocalElection(ids) }.getOrElse {
            assertEquals(it.message, BAD_RESPONSE)
        }
    }
}
