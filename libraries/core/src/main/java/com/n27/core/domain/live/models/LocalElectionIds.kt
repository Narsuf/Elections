package com.n27.core.domain.live.models

import java.io.Serializable

data class LocalElectionIds(
    val region: String,
    val province: String,
    val municipality: String
) : Serializable