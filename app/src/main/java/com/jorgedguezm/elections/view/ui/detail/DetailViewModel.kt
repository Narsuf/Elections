package com.jorgedguezm.elections.view.ui.detail

import android.content.Context
import android.widget.SimpleAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.models.entities.Election
import com.jorgedguezm.elections.utils.Utils

import javax.inject.Inject

class DetailViewModel @Inject constructor(context: Context, utils: Utils) : ViewModel() {

    val adapter: LiveData<SimpleAdapter>

    private val e: MutableLiveData<Election> = MutableLiveData()

    init {
        adapter = e.switchMap {
            val from = arrayOf("color", "partyName", "numberVotes", "votesPercentage", "elects")
            val to = intArrayOf(R.id.tvPartyColor, R.id.tvPartyName, R.id.tvNumberVotes,
                    R.id.tvVotesPercentage, R.id.tvElects)

            val arrayList = ArrayList<Map<String, Any>>()

            val sortedResults = it.results.sortedByDescending { r -> r.elects }

            for (r in sortedResults) {
                val map = HashMap<String, Any>()

                map[from[0]] = "#" + r.party.color
                map[from[1]] = r.party.name
                map[from[2]] = r.votes
                map[from[3]] = if (it.chamberName == "Senado")
                    "- %"
                else
                    utils.getPercentageWithTwoDecimals(r.votes, it.validVotes).toString() + " %"

                map[from[4]] = r.elects

                arrayList.add(map)
            }

            MutableLiveData<SimpleAdapter>().apply {
                postValue(SimpleAdapter(context, arrayList, R.layout.list_item_detail_activity, from, to))
            }
        }
    }

    fun postElection(election: Election) = e.postValue(election)
}