package com.jorgedguezm.elections.data.models

import com.squareup.moshi.Json

data class ApiResponse<T> (

    @Json(name = "data")
    val data: T
)
