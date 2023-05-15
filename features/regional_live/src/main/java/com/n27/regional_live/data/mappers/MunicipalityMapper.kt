package com.n27.regional_live.data.mappers

import com.n27.core.extensions.first
import com.n27.core.extensions.getKey
import com.n27.core.extensions.map
import com.n27.core.extensions.removeAccents
import com.n27.regional_live.domain.models.Municipality
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
