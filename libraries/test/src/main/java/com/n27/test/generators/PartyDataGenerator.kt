package com.n27.test.generators

import com.n27.core.domain.election.models.Party

fun getParties() = listOf(getParty())

fun getParty(
    id: Long = 1,
    name: String = "PP",
    color: String = "006EC7"
) = Party(id, name, color)