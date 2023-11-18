package com.n27.core.data.local.json.mappers

import com.n27.core.domain.region.models.Municipality
import com.n27.core.extensions.first
import com.n27.core.extensions.getKey
import com.n27.core.extensions.map
import com.n27.core.extensions.removeAccents
import org.json.JSONArray
import org.json.JSONObject

fun String.toMunicipalities(province: String) = toProvincesJSONArray()
    .first { it.getKey() == province }
    ?.getJSONArray(province)?.toMunicipalitiesList()
    ?: emptyList()

private fun String.toProvincesJSONArray() = JSONObject(this).getJSONArray("provinces")

private fun JSONArray.toMunicipalitiesList() = map { it.toMunicipality() }

private fun JSONObject.toMunicipality() = Municipality(
    getString("id"),
    getString("name").removeAccents()
)
