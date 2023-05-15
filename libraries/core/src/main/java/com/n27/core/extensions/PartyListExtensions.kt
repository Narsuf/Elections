package com.n27.core.extensions

import com.n27.core.domain.election.models.Party

fun List<Party>.lowercaseNames() = map {
    it.copy(name = it.name.lowercase())
}
