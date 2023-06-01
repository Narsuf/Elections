package com.n27.core.data

import com.n27.core.Constants.BAD_RESPONSE
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.common.DataUtils
import com.n27.core.data.local.json.JsonReader
import com.n27.core.data.local.room.ElectionDao
import com.n27.core.data.local.room.mappers.toParties
import com.n27.core.data.remote.api.ElDiarioApi
import com.n27.core.data.remote.api.mappers.toLiveElection
import com.n27.core.domain.live.models.LiveElection
import com.n27.core.domain.live.models.LiveElections
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.test.generators.getElDiarioParties
import com.n27.test.generators.getElDiarioParty
import com.n27.test.generators.getElDiarioResult
import com.n27.test.generators.getElection
import com.n27.test.generators.getLiveElection
import com.n27.test.generators.getLiveElections
import com.n27.test.generators.getMunicipalities
import com.n27.test.generators.getPartyRaw
import com.n27.test.generators.getProvinces
import com.n27.test.generators.getRegions
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
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
    private lateinit var api: ElDiarioApi
    private lateinit var dataUtils: DataUtils
    private lateinit var jsonReader: JsonReader
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val ids = LocalElectionIds("01", "04", "01")

    @Before
    fun setUp() = runBlocking {
        api = mock(ElDiarioApi::class.java)
        dataUtils = mock(DataUtils::class.java)
        jsonReader = mock(JsonReader::class.java)

        `when`(api.getRegionalParties(anyString())).thenReturn(getElDiarioParties())
        `when`(api.getLocalParties()).thenReturn(getElDiarioParties())
        `when`(dataUtils.isConnectedToInternet()).thenReturn(true)

        val regions = JsonReader().getStringJson("regions-test.json")
        val provinces = JsonReader().getStringJson("provinces-test.json")
        val municipalities = JsonReader().getStringJson("municipalities-test.json")
        `when`(jsonReader.getStringJson("regions.json")).thenReturn(regions)
        `when`(jsonReader.getStringJson("provinces.json")).thenReturn(provinces)
        `when`(jsonReader.getStringJson("municipalities.json")).thenReturn(municipalities)

        repository = LiveRepositoryImpl(api, dataUtils, jsonReader, moshi)
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

        repository.getRegionalElections().collect { assertEquals(expected, it) }
    }

    @Test
    fun getRegionalElectionsEmpty(): Unit = runBlocking {
        `when`(api.getRegionalResults()).thenReturn(listOf())

       repository.getRegionalElections().collect { result ->
           assertTrue(result.isFailure)
           result.onFailure { assertEquals(it.message, BAD_RESPONSE) }
       }
    }

    @Test
    fun getRegionalElection() = runBlocking {
        val expected = success(
            getElDiarioResult(id = "01")
                .toLiveElection("Autonómicas", "Andalucía", getElDiarioParties())
        )

        `when`(api.getRegionalResult(anyString())).thenReturn(getElDiarioResult(id = "01"))

        repository.getRegionalElection("01").collect { assertEquals(expected, it) }
    }

    @Test
    fun getRegionalElectionWithNoConnection(): Unit = runBlocking {
        `when`(dataUtils.isConnectedToInternet()).thenReturn(false)
        `when`(api.getRegionalResult(anyString())).thenReturn(null)

        runCatching { repository.getRegionalElection("01") }.getOrElse {
            assertEquals(it.message, NO_INTERNET_CONNECTION)
        }
    }

    @Test
    fun getRegionalElectionWithEmptyResponse(): Unit = runBlocking {
        `when`(api.getRegionalResult(anyString())).thenReturn(null)

        runCatching { repository.getRegionalElection("01") }.getOrElse {
            assertEquals(it.message, BAD_RESPONSE)
        }
    }

    @Test
    fun getLocalElection() = runBlocking {
        val expected = success(
            getElDiarioResult(id = "04001")
                .toLiveElection("Municipales", "Abla", getElDiarioParties())
        )

        `when`(api.getLocalResult(ids)).thenReturn(getElDiarioResult())

        repository.getLocalElection(ids).collect { assertEquals(expected, it) }
    }

    @Test
    fun getLocalElectionWithNoConnection(): Unit = runBlocking {
        `when`(dataUtils.isConnectedToInternet()).thenReturn(false)
        `when`(api.getLocalResult(ids)).thenReturn(null)

        runCatching { repository.getLocalElection(ids) }.getOrElse {
            assertEquals(it.message, NO_INTERNET_CONNECTION)
        }
    }

    @Test
    fun getLocalElectionWithEmptyResponse(): Unit = runBlocking {
        `when`(api.getLocalResult(ids)).thenReturn(null)

        runCatching { repository.getLocalElection(ids) }.getOrElse {
            assertEquals(it.message, BAD_RESPONSE)
        }
    }

    fun getRegionsFromJson() = runBlocking {
        val expected = success(getRegions())

        assertEquals(expected, repository.getRegions())
    }

    @Test
    fun getProvincesFromJson() = runBlocking {
        assertEquals(repository.getProvinces("Andalucía"), getProvinces())
    }

    @Test
    fun getMunicipalitiesFromJson() = runBlocking {
        assertEquals(repository.getMunicipalities("Almería"), getMunicipalities())
    }
}
