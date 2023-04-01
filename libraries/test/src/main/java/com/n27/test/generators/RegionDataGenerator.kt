package com.n27.test.generators

import com.n27.core.data.local.json.models.Municipality
import com.n27.core.data.local.json.models.Province
import com.n27.core.data.local.json.models.Region
import com.n27.core.data.local.json.models.Regions

fun getRegions() = Regions(
    listOf(
        getRegion(), getRegion("19", "Melilla")
    )
)

private fun getRegion(
    id: String = "01",
    name: String = "Andalucía"
) = Region(id, name)

fun getProvinces() = listOf(
    getProvince(), getProvince("11", "Cádiz")
)

private fun getProvince(
    id: String = "04",
    name: String = "Almería"
) = Province(id, name)

fun getMunicipalities() = listOf(
    getMunicipality(), getMunicipality("02", "Abrucena")
)

private fun getMunicipality(
    id: String = "01",
    name: String = "Abla"
) = Municipality(id, name)


