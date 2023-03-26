package com.n27.core.data.json.mappers

import com.n27.core.data.json.models.Municipalities
import org.json.JSONArray
import org.json.JSONObject

fun JSONArray.toMunicipalities() = Municipalities(
    provinces = mutableListOf<Municipalities.Province>().also {
        for (i in 0 until length()) {
            it.add(getJSONObject(i).toMunicipalityProvince())
        }
    }
)

private fun JSONObject.toMunicipalityProvince() = Municipalities.Province(
    municipalities = names()?.getString(0)
        ?.let { getJSONArray(it).toMunicipalitiesList() }
        ?: emptyList()
)

private fun JSONArray.toMunicipalitiesList() = mutableListOf<Municipalities.Province.Municipality>()
    .also {
        for (i in 0 until length()) {
            it.add(getJSONObject(i).toMunicipality())
        }
    }

private fun JSONObject.toMunicipality() = Municipalities.Province.Municipality(
    getString("id"),
    getString("name")
)