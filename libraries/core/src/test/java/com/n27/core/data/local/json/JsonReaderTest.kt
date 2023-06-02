package com.n27.core.data.local.json

import com.n27.test.jsons.RegionalResponses
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class JsonReaderTest {

    @Test
    fun readJson() = runBlocking {
        val json = JsonReader().getStringJson("regions-test.json").trimIndent()
        assertEquals(RegionalResponses.regions, json)
    }
}
