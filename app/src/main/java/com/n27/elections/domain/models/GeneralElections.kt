package com.n27.elections.domain.models

import com.n27.core.domain.election.models.Election

data class GeneralElections(val congress: List<Election>, val senate: List<Election>)