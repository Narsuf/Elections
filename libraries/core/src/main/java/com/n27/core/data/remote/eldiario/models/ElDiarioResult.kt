package com.n27.core.data.remote.eldiario.models

data class ElDiarioResult(
    val id: String,
    val date: Long,
    val abstentions: Int,
    val blankVotes: Int,
    val census: Int,
    val scrutinized: Int,
    val nullVotes: Int,
    val validVotes: Int,
    val seats: Int,
    val partiesResults: List<ElDiarioPartyResult>
)