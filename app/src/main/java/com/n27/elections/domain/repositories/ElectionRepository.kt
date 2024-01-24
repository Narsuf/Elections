package com.n27.elections.domain.repositories

import com.n27.core.domain.election.models.Election
import com.n27.core.domain.election.models.Elections

interface ElectionRepository {

    suspend fun getElectionsLocally(): Elections?
    suspend fun getElectionsRemotely(): Result<Elections>
    suspend fun saveElections(elections: List<Election>)
}