package com.n27.core.extensions

import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class JSONObjectExtensionsTest {

    @Test
    fun getKey() {
        val expected = "id"
        val json = JSONObject().put(expected, 1)

        assertEquals(expected, json.getKey())
    }

    @Test
    fun getNullKey() { assertNull(JSONObject().getKey()) }
}
