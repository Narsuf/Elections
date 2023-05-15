package com.n27.core.data.local

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class JsonReaderTest {

    @Test
    fun readJson() = runBlocking {
        val json = JsonReader().getStringJson("elections-test.json").trimIndent()
        assertEquals(JsonReaderResponses.elections, json)
    }
}
