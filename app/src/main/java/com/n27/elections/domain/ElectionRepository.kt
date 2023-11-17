package com.n27.elections.domain

import com.n27.core.domain.election.Election
import com.n27.core.domain.election.Elections

interface ElectionRepository {

    suspend fun getElectionsLocally(): Elections?
    suspend fun getElectionsRemotely(): Result<Elections>
    suspend fun saveElections(elections: List<Election>)
}