package com.jorgedguezm.elections.presentation.common.extensions

import com.jorgedguezm.elections.data.models.Result

fun List<Result>.getElects() = ArrayList<Int>(map { it.elects }).toTypedArray()
fun List<Result>.getColors() = ArrayList<String>(map { "#${it.party.color}" }).toTypedArray()
