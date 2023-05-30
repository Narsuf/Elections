package com.n27.regional_live.data.mappers

import com.n27.core.data.local.JsonReader
import com.n27.test.generators.getMunicipalities
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class MunicipalityMapperTest {

    @Test
    fun `should return expected municipalities`() = runBlocking {
        val expected = getMunicipalities()
        val actual = JsonReader()
            .getStringJson("municipalities-test.json")
            .toMunicipalities("Almería")

        assertEquals(expected, actual)
    }
}
