package com.n27.core.data.json.models

data class Provinces(val regions: List<Region>) {

    data class Region(val provinces: List<Province>) {

        data class Province(val id: String, val name: String)
    }
}
