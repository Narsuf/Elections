package com.n27.core.data.api.mappers

import com.n27.core.data.models.Result

internal fun getEmptyResult() = Result(
    id = 0,
    partyId = 0,
    electionId = 0,
    elects = 0,
    votes = 0,
    party = getEmptyParty()
)
