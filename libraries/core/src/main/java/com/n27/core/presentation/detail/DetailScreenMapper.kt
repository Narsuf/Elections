package com.n27.core.presentation.detail

import com.n27.core.R
import com.n27.core.data.models.Election
import com.n27.core.extensions.divide
import com.n27.core.presentation.detail.entities.DetailState.Content


// TODO: Test.
internal fun Election.toContent(): Content {
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
        map[keys[2]] = java.text.NumberFormat.getIntegerInstance().format(r.votes)
        map[keys[3]] = if (chamberName == com.n27.core.Constants.KEY_SENATE)
            "- %"
        else
            r.votes.divide(validVotes) + " %"

        map[keys[4]] = r.elects

        arrayList.add(map)
    }

    return Content(this, arrayList, keys.toList(), resources.toList())
}