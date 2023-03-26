package com.n27.core.data.json.models

data class Municipalities(val provinces: List<Province>) {

    data class Province(val municipalities: List<Municipality>) {

        data class Municipality(val id: String, val name: String)
    }
}
