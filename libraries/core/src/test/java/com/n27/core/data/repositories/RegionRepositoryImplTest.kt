package com.n27.core.data.repositories

import com.n27.core.data.local.json.JsonReader
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.core.domain.region.RegionRepository
import com.n27.test.generators.getMunicipalities
import com.n27.test.generators.getProvinces
import com.n27.test.generators.getRegions
import com.n27.test.jsons.RegionalResponses
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import kotlin.Result.Companion.success

@RunWith(RobolectricTestRunner::class)
class RegionRepositoryImplTest {

    private lateinit var repository: RegionRepository
    private lateinit var jsonReader: JsonReader
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Before
    fun setUp() = runBlocking {
        jsonReader = Mockito.mock(JsonReader::class.java)

        `when`(jsonReader.getStringJson("regions.json")).thenReturn(RegionalResponses.regions)
        `when`(jsonReader.getStringJson("provinces.json")).thenReturn(RegionalResponses.provinces)
        `when`(jsonReader.getStringJson("municipalities.json")).thenReturn(RegionalResponses.municipalities)

        repository = RegionRepositoryImpl(jsonReader, moshi)
    }

    @Test
    fun getMunicipalityName() = runBlocking {
        val actual = repository.getMunicipalityName(
            getRegions(),
            LocalElectionIds("01", "04", "01")
        )

        assertEquals("Abla", actual)
    }

    @Test
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