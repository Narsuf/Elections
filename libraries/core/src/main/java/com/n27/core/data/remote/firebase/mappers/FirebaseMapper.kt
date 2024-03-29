package com.n27.core.data.remote.firebase.mappers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.GenericTypeIndicator
import com.n27.core.domain.election.models.Election

fun DataSnapshot.toElections() = getValue(object : GenericTypeIndicator<List<Election>>() {})
