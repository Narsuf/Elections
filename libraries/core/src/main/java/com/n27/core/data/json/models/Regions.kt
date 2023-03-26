package com.n27.core.data.json.models

data class Regions(val regions: List<Region>) {

    data class Region(val id: String, val name: String)
}


