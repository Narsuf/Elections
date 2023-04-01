package com.n27.elections.data.api.mappers

import com.n27.core.extensions.sortByDateAndFormat
import com.n27.core.extensions.sortResultsByElectsAndVotes
import com.n27.elections.data.api.models.ApiResponse

fun ApiResponse.toElections() = elections
    .map { it.sortResultsByElectsAndVotes() }
    .sortByDateAndFormat()
