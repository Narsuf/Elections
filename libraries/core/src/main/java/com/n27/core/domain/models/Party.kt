package com.n27.core.domain.models

import java.io.Serializable

data class Party(
    val id: Long = 0,
    val name: String = "",
    val color: String = ""
) : Serializable

