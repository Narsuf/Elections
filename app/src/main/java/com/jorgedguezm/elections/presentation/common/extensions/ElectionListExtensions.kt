package com.jorgedguezm.elections.presentation.common.extensions

import com.jorgedguezm.elections.data.models.Election

fun List<Election>.sortByDateAndFormat(): List<Election> {
    return sortedByDescending { it.formatDateToDouble() }.map { it.formatDate() }
}

private fun Election.formatDateToDouble(): Double {
    return if (date.length > 4)
        date.toDouble() / 10
    else
        date.toDouble()
}

private fun Election.formatDate(): Election {
    var newDate = date

    if (date.length > 4) {
        newDate = when (date) {
            "20192" -> "2019-10N"
            "20191" -> "2019-28A"
            else -> date
        }
    }

    return copy(date = newDate)
}

fun Election.sortResultsByElectsAndVotes() = copy(
    results = results
        .sortedByDescending { it.votes }
        .sortedByDescending { it.elects }
)

