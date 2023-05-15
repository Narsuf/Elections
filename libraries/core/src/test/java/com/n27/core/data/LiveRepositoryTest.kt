package com.n27.core.data

import com.n27.core.Constants.BAD_RESPONSE
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.common.DataUtils
import com.n27.core.data.local.json.JsonReader
import com.n27.core.data.local.room.ElectionDao
import com.n27.core.data.local.room.mappers.toParties
import com.n27.core.data.remote.api.ElPaisApi
import com.n27.core.data.remote.api.mappers.toElection
import com.n27.core.data.remote.api.mappers.toElectionXml
import com.n27.core.data.remote.api.models.ElectionXml
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.extensions.lowercaseNames
import com.n27.core.extensions.toStringId
import com.n27.test.generators.getMunicipalities
import com.n27.test.generators.getPartyRaw
import com.n27.test.generators.getProvinces
import com.n27.test.generators.getRegions
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LiveRepositoryTest {

    private lateinit var repository: LiveRepository
    private lateinit var service: ElPaisApi
    private lateinit var dao: ElectionDao
    private lateinit var jsonReader: JsonReader
    private lateinit var dataUtils: DataUtils
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val ids = LocalElectionIds("", "", "")

    @Before
    fun setUp() {
        service = mock(ElPaisApi::class.java)
        dao = mock(ElectionDao::class.java)
        jsonReader = mock(JsonReader::class.java)
        dataUtils = mock(DataUtils::class.java)

        `when`(dataUtils.isConnectedToInternet()).thenReturn(true)

        repository = LiveRepository(service, dao, jsonReader, moshi, dataUtils)
    }

    @Test
    fun getRegionalElections() = runBlocking {
        val response = JsonReader().getStringJson("regional-election-test.xml")
        val election = response.toElectionXml()
        val elections = mutableListOf<ElectionXml>()

        for (i in 1..17) {
            elections.add(
                election.copy(id = i.toStringId())
            )
        }

        `when`(service.getRegionalElection(anyString())).thenReturn(response)

        assertEquals(repository.getRegionalElections(), elections)
    }

    @Test
    fun getRegionalElectionsEmpty(): Unit = runBlocking {
        `when`(service.getRegionalElection(anyString())).thenReturn(null)

        runCatching { repository.getRegionalElections() }.getOrElse {
            assertEquals(it.message, BAD_RESPONSE)
        }
    }

    @Test
    fun getRegionalElection() = runBlocking {
        val response = JsonReader().getStringJson("regional-election-test.xml")
        val parties = listOf(getPartyRaw(), getPartyRaw(name = "PSOE"))
        val expected = response.toElection(parties.toParties())

        `when`(dao.getParties()).thenReturn(parties)
        `when`(service.getRegionalElection(anyString())).thenReturn(response)

        assertEquals(expected, repository.getRegionalElection("01"))
    }

    @Test
    fun getRegionalElectionWithNoConnection(): Unit = runBlocking {
        `when`(dataUtils.isConnectedToInternet()).thenReturn(false)
        `when`(dao.getParties()).thenReturn(listOf())
        `when`(service.getRegionalElection(anyString())).thenReturn(null)

        runCatching { repository.getRegionalElection("01") }.getOrElse {
            assertEquals(it.message, NO_INTERNET_CONNECTION)
        }
    }

    @Test
    fun getRegionalElectionWithEmptyResponse(): Unit = runBlocking {
        `when`(dao.getParties()).thenReturn(listOf())
        `when`(service.getRegionalElection(anyString())).thenReturn(null)

        runCatching { repository.getRegionalElection("01") }.getOrElse {
            assertEquals(it.message, BAD_RESPONSE)
        }
    }

    @Test
    fun getLocalElection() = runBlocking {
        val response = JsonReader().getStringJson("local-election-test.xml")
        val parties = listOf(getPartyRaw(), getPartyRaw(name = "PSOE-A"))
        val expected = response.toElection(parties.toParties())

        `when`(dao.getParties()).thenReturn(parties)
        `when`(service.getLocalElection(ids)).thenReturn(response)

        assertEquals(expected, repository.getLocalElection(ids))
    }

    @Test
    fun getLocalElectionWithNoConnection(): Unit = runBlocking {
        `when`(dataUtils.isConnectedToInternet()).thenReturn(false)
        `when`(dao.getParties()).thenReturn(listOf())
        `when`(service.getLocalElection(ids)).thenReturn(null)

        runCatching { repository.getLocalElection(ids) }.getOrElse {
            assertEquals(it.message, NO_INTERNET_CONNECTION)
        }
    }

    @Test
    fun getLocalElectionWithEmptyResponse(): Unit = runBlocking {
        `when`(dao.getParties()).thenReturn(listOf())
        `when`(service.getLocalElection(ids)).thenReturn(null)

        runCatching { repository.getLocalElection(ids) }.getOrElse {
            assertEquals(it.message, BAD_RESPONSE)
        }
    }

    @Test
    fun getRegionsFromJson() = runBlocking {
        val regions = JsonReader().getStringJson("regions-test.json")

        `when`(jsonReader.getStringJson(anyString())).thenReturn(regions)

        assertEquals(repository.getRegions(), getRegions())
    }

    @Test
    fun getProvincesFromJson() = runBlocking {
        val provinces = JsonReader().getStringJson("provinces-test.json")

        `when`(jsonReader.getStringJson(anyString())).thenReturn(provinces)

        assertEquals(repository.getProvinces("Andalucía"), getProvinces())
    }

    @Test
    fun getMunicipalitiesFromJson() = runBlocking {
        val municipalities = JsonReader().getStringJson("municipalities-test.json")

        `when`(jsonReader.getStringJson(anyString())).thenReturn(municipalities)

        assertEquals(repository.getMunicipalities("Almería"), getMunicipalities())
    }
}
