package com.n27.core.data.local.json.mappers

import com.n27.test.generators.getProvinces
import com.n27.test.jsons.RegionalResponses
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ProvinceMapperTest {

    @Test
    fun `should return expected provinces`() = runBlocking {
        val expected = getProvinces()
        val actual = RegionalResponses.provinces.toProvinces("Andalucía")

        assertEquals(expected, actual)
    }
}
