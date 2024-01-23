package com.n27.core.presentation.detail.dialog.mappers

import com.n27.core.domain.election.models.Election
import com.n27.core.extensions.divide
import com.n27.core.presentation.detail.dialog.models.Info
import java.text.NumberFormat.getIntegerInstance

fun Election.toInfoList() = listOf(
    Info(value = getIntegerInstance().format(totalElects)),
    Info(
        value = getIntegerInstance().format(validVotes),
        percentage = validVotes.divide(validVotes + abstentions)
    ),
    Info(
        value = getIntegerInstance().format(abstentions),
        percentage = abstentions.divide(validVotes + abstentions)
    ),
    Info(
        value = getIntegerInstance().format(nullVotes),
        percentage = nullVotes.divide(validVotes)
    ),
    Info(
        value = getIntegerInstance().format(blankVotes),
        percentage = blankVotes.divide(validVotes)
    )
)