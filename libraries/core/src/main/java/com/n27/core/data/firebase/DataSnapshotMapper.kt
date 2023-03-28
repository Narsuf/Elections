package com.n27.core.data.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.GenericTypeIndicator
import com.n27.core.data.models.Election

fun DataSnapshot.toElections() = getValue(object : GenericTypeIndicator<List<Election>>() { })
