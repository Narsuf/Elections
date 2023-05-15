package com.n27.core.domain.election.models

import java.io.Serializable

data class Result(
    val id: Long = 0,
    val partyId: Long = 0,
    val electionId: Long = 0,
    val elects: Int = 0,
    val votes: Int = 0,
    val party: Party = Party()
) : Serializable
