package com.n27.core.presentation.detail.mappers

import com.n27.core.Constants.KEY_SENATE
import com.n27.core.R
import com.n27.core.domain.election.models.Election
import com.n27.core.extensions.divide
import com.n27.core.presentation.detail.models.DetailContentState.WithData
import java.text.NumberFormat.getNumberInstance

internal fun Election.toContent(): WithData {
    val keys = arrayOf("color", "partyName", "numberVotes", "votesPercentage", "elects")
    val resources = intArrayOf(
        R.id.color_list_item_activity_detail,
        R.id.party_list_item_activity_detail,
        R.id.votes_list_item_activity_detail,
        R.id.percentage_list_item_activity_detail,
        R.id.elects_list_item_activity_detail
    )

    val arrayList = ArrayList<Map<String, Any>>()

    for (r in results) {
        val map = HashMap<String, Any>()

        map[keys[0]] = "#" + r.party.color
        map[keys[1]] = r.party.name
        map[keys[2]] = getNumberInstance().format(r.votes)
        map[keys[3]] = if (chamberName == KEY_SENATE)
            "- %"
        else
            r.votes.divide(validVotes) + " %"

        map[keys[4]] = r.elects

        arrayList.add(map)
    }

    return WithData(this, arrayList, keys.toList(), resources.toList())
}
