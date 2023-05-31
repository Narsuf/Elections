package com.n27.core.data.local.json.mappers

import com.n27.core.domain.live.models.Province
import com.n27.core.extensions.first
import com.n27.core.extensions.getKey
import com.n27.core.extensions.map
import org.json.JSONArray
import org.json.JSONObject

fun String.toProvinces(region: String) = toRegionsJSONArray()
    .first { it.getKey() == region }
    ?.getJSONArray(region)?.toProvinceList()
    ?: emptyList()

private fun String.toRegionsJSONArray() = JSONObject(this).getJSONArray("regions")

private fun JSONArray.toProvinceList() = map { it.toProvince() }

private fun JSONObject.toProvince() = Province(
    getString("id"),
    getString("name")
)
