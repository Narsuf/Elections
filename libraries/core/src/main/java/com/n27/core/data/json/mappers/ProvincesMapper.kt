package com.n27.core.data.json.mappers

import com.n27.core.data.json.models.Provinces
import org.json.JSONArray
import org.json.JSONObject

fun JSONArray.toProvinces() = Provinces(
    regions = mutableListOf<Provinces.Region>().also {
        for (i in 0 until length()) {
            it.add(getJSONObject(i).toRegion())
        }
    }
)

private fun JSONObject.toRegion() = Provinces.Region(
    provinces = names()?.getString(0)
        ?.let { getJSONArray(it).toProvinceList() }
        ?: emptyList()
)

private fun JSONArray.toProvinceList() = mutableListOf<Provinces.Region.Province>().also {
    for (i in 0 until length()) {
        it.add(getJSONObject(i).toProvince())
    }
}

private fun JSONObject.toProvince() = Provinces.Region.Province(
    getString("id"),
    getString("name")
)