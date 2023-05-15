package com.n27.regional_live.data.mappers

import com.n27.core.data.local.json.JsonReader
import com.n27.test.generators.getProvinces
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
        val actual = JsonReader()
            .getStringJson("provinces-test.json")
            .toProvinces("Andalucía")

        assertEquals(expected, actual)
    }
}
