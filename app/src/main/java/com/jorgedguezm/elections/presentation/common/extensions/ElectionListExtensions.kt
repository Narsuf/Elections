package com.jorgedguezm.elections.presentation.common.extensions

import com.jorgedguezm.elections.data.models.Election

fun List<Election>.sortByDate(): List<Election> {
    sortedByDescending {
        if (it.date.length > 4)
            it.date.toDouble() / 10
        else
            it.date.toDouble()
    }.let { sortedElections ->
        sortedElections.forEach {
            if (it.date.length > 4) {
                it.date = when (it.date) {
                    "20192" -> "2019-10N"
                    "20191" -> "2019-28A"
                    else -> it.date
                }
            }
        }

        return sortedElections
    }
}

fun List<Election>.sortResultsByElectsAndVotes(): List<Election> {
    val sortedElections = mutableListOf<Election>()

    forEach { election ->
        val sortedResults = election.result.sortedByDescending { it.votes }.sortedByDescending { it.elects }
        sortedElections.add(election.copy(result = sortedResults))
    }

    return sortedElections
}
