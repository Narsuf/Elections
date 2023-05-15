package com.n27.core.domain.election

import com.n27.core.domain.election.models.Elections

interface ElectionRepository {

    suspend fun getElections(): Result<Elections>
}