package com.jorgedguezm.elections.data.models

import com.squareup.moshi.Json

data class ApiResponse (

    @Json(name = "elections")
    val elections: List<Election>
)
