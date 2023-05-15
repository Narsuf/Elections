package com.n27.core.domain

import com.n27.core.domain.models.Elections

interface ElectionRepository {

    suspend fun getElections(): Result<Elections>
}