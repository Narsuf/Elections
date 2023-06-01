package com.n27.core.data.remote.api.mappers

import com.n27.core.data.local.json.JsonReader
import com.n27.core.data.remote.api.models.ElDiarioParty
import kotlinx.coroutines.runBlocking
import org.intellij.lang.annotations.Language
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ElDiarioPartyMapper {

    @Language("json")
    val localParties = """
        {
          "1000": {
            "group": "1277",
            "sigla": "Junts",
            "color": "#40B6A4",
            "block": "r"
          },
          "1001": {
            "group": "1277",
            "sigla": "Junts",
            "color": "#40B6A4",
            "block": "r"
          },
          "1002": {
            "group": "6060",
            "sigla": "CUP",
            "color": "#FFED00",
            "block": "l"
          }
        }

    """.trimIndent()

    @Test
    fun `should return expected ElDiarioParties`() = runBlocking {
        val actual = localParties.toElDiarioParties()

        val expected = listOf(
            ElDiarioParty(id = "1000", name = "Junts", color = "40B6A4"),
            ElDiarioParty(id = "1001", name = "Junts", color = "40B6A4"),
            ElDiarioParty(id = "1002", name = "CUP", color = "FFED00")
        )

        Assert.assertEquals(expected, actual)
    }
}