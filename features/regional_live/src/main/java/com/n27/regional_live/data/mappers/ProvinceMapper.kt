package com.n27.regional_live.data.mappers

import com.n27.core.extensions.first
import com.n27.core.extensions.getKey
import com.n27.core.extensions.map
import com.n27.regional_live.domain.models.Province
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
