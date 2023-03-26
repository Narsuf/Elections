package com.n27.core.extensions

import org.json.JSONArray
import org.json.JSONObject

fun <T> JSONArray.map(transform: (JSONObject) -> T): List<T> {
    val list = mutableListOf<T>()

    for (i in 0 until length()) {
        val element = getJSONObject(i)
        list.add(transform(element))
    }

    return list
}

fun JSONArray.first(predicate: (JSONObject) -> Boolean): JSONObject? {
    for (i in 0 until length()) {
        val element = getJSONObject(i)
        if (predicate(element)) return element
    }

    return null
}