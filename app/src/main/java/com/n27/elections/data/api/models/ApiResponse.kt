package com.n27.elections.data.api.models

import com.n27.core.domain.election.models.Election

data class ApiResponse(val elections: List<Election>)
