package org.n27.elections.data.models

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

data class Result(
        val id: Long = 0,
        val partyId: Long = 0,
        val electionId: Long = 0,
        val elects: Int = 0,
        val votes: Int = 0,
        val party: Party = Party()
) : Serializable

data class Party(
        val id: Long = 0,
        val name: String = "",
        val color: String = ""
) : Serializable
