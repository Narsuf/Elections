package com.jorgedguezm.elections.models.network

data class ErrorEnvelope(
        val status_code: Int,
        val type: String,
        val message: String,
        val success: Boolean
)