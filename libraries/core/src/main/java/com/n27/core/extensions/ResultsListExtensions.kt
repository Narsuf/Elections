package com.n27.core.extensions

import com.n27.core.domain.models.Result

fun List<Result>.getElects() = ArrayList<Int>(map { it.elects }).toTypedArray()
fun List<Result>.getColors() = ArrayList<String>(map { "#${it.party.color}" }).toTypedArray()
