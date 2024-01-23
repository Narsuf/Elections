package com.n27.core.domain.election.models

import java.io.Serializable

data class Election(
        val id: Long = 0,
        val name: String = "",
        val date: String = "",
        val place: String = "",
        val chamberName: String = "",
        val totalElects: Int = 0,
        val scrutinized: Float = 0f,
        val validVotes: Int = 0,
        val abstentions: Int = 0,
        val blankVotes: Int = 0,
        val nullVotes: Int = 0,
        val results: List<Result> = listOf()
) : Serializable
