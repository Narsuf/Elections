package com.n27.core.data.local.json.mappers

import com.n27.core.data.local.json.models.Municipality
import com.n27.core.extensions.first
import com.n27.core.extensions.getKey
import com.n27.core.extensions.map
import org.json.JSONArray
import org.json.JSONObject

fun JSONArray.toMunicipalities(province: String) = first { it.getKey() == province }
    ?.getJSONArray(province)?.toMunicipalitiesList()
    ?: emptyList()

private fun JSONArray.toMunicipalitiesList() = map { it.toMunicipality() }

private fun JSONObject.toMunicipality() = Municipality(
    getString("id"),
    getString("name")
)
