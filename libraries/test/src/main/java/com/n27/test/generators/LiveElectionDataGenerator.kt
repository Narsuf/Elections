package com.n27.test.generators

import com.n27.core.domain.election.Election
import com.n27.core.domain.live.models.LiveElection
import com.n27.core.domain.live.models.LiveElections

fun getLiveElections() = LiveElections(items = getLiveElectionList())

fun getLiveElectionList() = listOf(getLiveElection())

fun getLiveElection(election: Election = getElection()) = LiveElection(id = "01", election)
