package com.n27.core.data.local.json.mappers

import com.n27.core.data.local.json.models.Province
import com.n27.core.extensions.first
import com.n27.core.extensions.getKey
import com.n27.core.extensions.map
import org.json.JSONArray
import org.json.JSONObject

fun JSONArray.toProvinces(region: String) = first { it.getKey() == region }
    ?.getJSONArray(region)?.toProvinceList()
    ?: emptyList()

private fun JSONArray.toProvinceList() = map { it.toProvince() }

private fun JSONObject.toProvince() = Province(
    getString("id"),
    getString("name")
)
