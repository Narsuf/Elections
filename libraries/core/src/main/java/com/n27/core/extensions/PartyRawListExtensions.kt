package com.n27.core.extensions

import com.n27.core.data.local.room.models.PartyRaw

fun List<PartyRaw>.lowercaseNames() = map { it.apply { name = name.lowercase() } }
