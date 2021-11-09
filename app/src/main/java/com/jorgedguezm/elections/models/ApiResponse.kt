package com.jorgedguezm.elections.models

import com.squareup.moshi.Json

data class ApiResponse (

    @Json(name = "data")
    val elections: List<Election>
)