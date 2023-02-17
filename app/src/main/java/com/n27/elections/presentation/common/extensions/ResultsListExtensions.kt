package com.n27.elections.presentation.common.extensions

import com.n27.elections.data.models.Result

fun List<Result>.getElects() = ArrayList<Int>(map { it.elects }).toTypedArray()
fun List<Result>.getColors() = ArrayList<String>(map { "#${it.party.color}" }).toTypedArray()
