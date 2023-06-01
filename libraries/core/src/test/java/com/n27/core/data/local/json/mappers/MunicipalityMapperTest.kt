package com.n27.core.data.local.json.mappers

import com.n27.core.data.local.json.JsonReader
import com.n27.test.generators.getMunicipalities
import com.n27.test.jsons.RegionalResponses
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
        val actual = RegionalResponses.municipalities.toMunicipalities("Almería")

        assertEquals(expected, actual)
    }
}
