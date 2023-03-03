package com.n27.core.presentation.common.extensions

import com.n27.core.data.models.Result

fun List<Result>.getElects() = ArrayList<Int>(map { it.elects }).toTypedArray()
fun List<Result>.getColors() = ArrayList<String>(map { "#${it.party.color}" }).toTypedArray()
