package com.n27.core.data.remote.api.mappers

import com.n27.core.data.local.json.JsonReader
import com.n27.core.data.remote.api.models.ElDiarioParty
import com.n27.core.data.remote.api.models.ElDiarioPartyResult
import com.n27.test.generators.getElDiarioParty
import com.n27.test.generators.getElDiarioPartyResult
import com.n27.test.generators.getElDiarioResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ElDiarioPartyMapper {

    @Test
    fun `should return expected ElDiarioParties`() = runBlocking {
        val actual = JsonReader()
            .getStringJson("local-election-parties-test.json")
            .toElDiarioParties()

        val expected = listOf(
            ElDiarioParty(id = "1000", name = "Junts", color = "40B6A4"),
            ElDiarioParty(id = "1001", name = "Junts", color = "40B6A4"),
            ElDiarioParty(id = "1002", name = "CUP", color = "FFED00")
        )

        Assert.assertEquals(expected, actual)
    }
}