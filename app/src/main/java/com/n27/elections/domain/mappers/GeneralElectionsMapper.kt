package com.n27.elections.domain.mappers

import com.n27.core.domain.election.models.Elections
import com.n27.elections.domain.models.GeneralElections

fun Elections.toGeneralElections() = GeneralElections(
    congress = items.filter { it.chamberName == "Congreso" },
    senate = items.filter { it.chamberName == "Senado" }
)