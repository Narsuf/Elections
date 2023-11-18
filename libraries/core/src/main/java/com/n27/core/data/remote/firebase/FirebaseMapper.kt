package com.n27.core.data.remote.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.GenericTypeIndicator
import com.n27.core.domain.election.Election

fun DataSnapshot.toElections() = getValue(object : GenericTypeIndicator<List<Election>>() {})
