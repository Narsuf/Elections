package com.n27.core.extensions

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class JSONArrayExtensionsTest {

    @Test
    fun map() {
        val array = JSONArray().apply {
            put(JSONObject().put("id", 1))
            put(JSONObject().put("id", 2))
        }

        val jsonObject3 = JSONObject().put("id", 3)
        val transformedArray = array.map { jsonObject3 }

        assertEquals(transformedArray[0], jsonObject3)
        assertEquals(transformedArray[1], jsonObject3)
    }

    @Test
    fun first() {
        val expected = JSONObject().put("id", 2)
        val array = JSONArray().apply {
            put(JSONObject().put("i", 1))
            put(expected)
            put(JSONObject().put("idd", 3))
        }

        val first = array.first { it.getKey() == "id" }

        assertEquals(expected, first)
    }
}
