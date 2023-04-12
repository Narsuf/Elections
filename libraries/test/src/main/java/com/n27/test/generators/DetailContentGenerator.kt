package com.n27.test.generators

import com.n27.core.R
import com.n27.core.presentation.detail.models.DetailContentState.WithData

fun getDetailContent() = WithData(getElection(), getArrayList(), getKeys(), getResources())

private fun getKeys() = listOf("color", "partyName", "numberVotes", "votesPercentage", "elects")

private fun getArrayList() = arrayListOf(getMap())

private fun getMap() = mutableMapOf<String, Any>().apply {
    put(getKeys()[0], "#006EC7")
    put(getKeys()[1], "PP")
    put(getKeys()[2], "7,215,530")
    put(getKeys()[3], "28.46 %")
    put(getKeys()[4], 123)
}.toMap()

private fun getResources() = listOf(
    R.id.color_list_item_activity_detail,
    R.id.party_list_item_activity_detail,
    R.id.votes_list_item_activity_detail,
    R.id.percentage_list_item_activity_detail,
    R.id.elects_list_item_activity_detail
)
