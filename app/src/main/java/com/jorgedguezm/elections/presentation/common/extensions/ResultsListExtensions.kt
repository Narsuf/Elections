package com.jorgedguezm.elections.presentation.common.extensions

import com.jorgedguezm.elections.data.models.Result

fun List<Result>.getElects(): Array<Int> {
    val elects = ArrayList<Int>()

    for (result in this) elects.add(result.elects)

    return elects.toTypedArray()
}

fun List<Result>.getColors(): Array<String> {
    val colors = ArrayList<String>()

    for (result in this) colors.add("#" + result.party.color)

    return colors.toTypedArray()
}
