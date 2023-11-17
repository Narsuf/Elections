package com.n27.core.data.remote.api.mappers

import com.n27.core.data.remote.api.models.ElDiarioPartyResult
import kotlinx.coroutines.runBlocking
import org.intellij.lang.annotations.Language
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ElDiarioPartyResultMapperTest {

    @Language("json")
    val partyResults = """
            {
              "0083": {
                "v": 9104,
                "s": 1,
                "p": 2959
              },
              "0017": {
                "v": 8925,
                "s": 0,
                "p": 2901
              }
            }
        """.trimIndent()

    @Test
    fun `should return expected ElDiarioPartyResults`() = runBlocking {
        val actual = JSONObject(partyResults).getElDiarioPartiesResults()

        val expected = listOf(
            ElDiarioPartyResult(id = "0083", votes = 9104, percentage = 2959, seats = 1)
        )

        assertEquals(expected, actual)
    }
}