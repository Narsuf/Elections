package com.n27.core.extensions

import com.n27.core.components.charts.PieChartData
import com.n27.core.components.charts.PieChartSlice
import com.n27.core.domain.election.models.Result

fun List<Result>.getPieChartData() = PieChartData(
    map { PieChartSlice(it.elects.toFloat(), "#${it.party.color}") }
)
