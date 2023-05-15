package com.n27.regional_live.data

import com.n27.core.data.local.JsonReader
import com.n27.test.generators.getMunicipalities
import com.n27.test.generators.getProvinces
import com.n27.test.generators.getRegions
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import kotlin.Result.Companion.success

@RunWith(RobolectricTestRunner::class)
class RegionRepositoryImplTest {

    private lateinit var repository: RegionRepositoryImpl
    private lateinit var jsonReader: JsonReader
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Before
    fun setUp() {
        jsonReader = mock(JsonReader::class.java)
        repository = RegionRepositoryImpl(jsonReader, moshi)
    }

    @Test
    fun getRegionsFromJson() = runBlocking {
        val regions = JsonReader().getStringJson("regions-test.json")
        val expected = success(getRegions())

        `when`(jsonReader.getStringJson(anyString())).thenReturn(regions)

        assertEquals(expected, repository.getRegions())
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