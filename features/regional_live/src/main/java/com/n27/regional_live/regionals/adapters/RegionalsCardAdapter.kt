package com.n27.regional_live.regionals.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.n27.core.data.local.room.models.PartyRaw
import com.n27.core.data.models.Election
import com.n27.core.data.remote.api.mappers.toElection
import com.n27.core.data.remote.api.models.ElectionXml
import com.n27.core.extensions.drawWithResults
import com.n27.regional_live.R

typealias OnLiveElectionClicked = (id: String?) -> Unit

class RegionalCardAdapter(
    private val elections: List<ElectionXml>,
    private val parties: List<PartyRaw>,
    private val onElectionClicked: OnLiveElectionClicked
) : RecyclerView.Adapter<RegionalCardAdapter.MyViewHolder>() {

    class MyViewHolder(val card: CardView) : RecyclerView.ViewHolder(card)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val card = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_regional_election, parent, false) as CardView

        return MyViewHolder(card)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val card = holder.card

        val electionXml = elections[position]
        val election = electionXml.toElection(parties)

        card.findViewById<TextView>(R.id.card_region_name).text = election.place
        card.setOnClickListener { onElectionClicked(electionXml.id) }
        (card.findViewById(R.id.card_regional_pie_chart) as PieChart).drawWithResults(election.results)
    }

    override fun getItemCount() = elections.size
}
