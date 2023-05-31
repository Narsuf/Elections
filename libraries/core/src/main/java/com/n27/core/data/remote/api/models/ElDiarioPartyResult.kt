package com.n27.core.data.remote.api.models

data class ElDiarioPartyResult(
    val id: String,
    val votes: Int,
    val percentage: Int,
    val seats: Int
)