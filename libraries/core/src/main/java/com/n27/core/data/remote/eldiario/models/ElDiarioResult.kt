package com.n27.core.data.remote.eldiario.models

import com.n27.core.domain.election.models.Result

data class ElDiarioResult(
    val abstentions: Int,
    val blankVotes: Int,
    val census: Int,
    val scrutinized: Int,
    val nullVotes: Int,
    val validVotes: Int,
    val seats: Int,
    val parties: List<ElDiarioPartyResult>
) {

    data class ElDiarioPartyResult(
        val id: String,
        val votes: Int,
        val percentage: Int,
        val seats: Int
    )
}