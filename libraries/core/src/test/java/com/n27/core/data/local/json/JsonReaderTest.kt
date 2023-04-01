package com.n27.core.data.local.json

import kotlinx.coroutines.runBlocking
import org.intellij.lang.annotations.Language
import org.junit.Assert.assertEquals
import org.junit.Test

class JsonReaderTest {

    @Test
    fun readJson() = runBlocking {
        val json = JsonReader().getStringJson("elections-test.json").trimIndent()
        assertEquals(JsonReaderResponses.elections, json)
    }
}
