package com.n27.regional_live.presentation.regionals.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.n27.core.domain.live.models.LiveElection
import com.n27.core.domain.live.models.LiveElections
import com.n27.core.extensions.drawWithResults
import com.n27.regional_live.R

typealias OnRegionalElectionClicked = (election: LiveElection) -> Unit

class RegionalCardAdapter(
    internal val onElectionClicked: OnRegionalElectionClicked
) : RecyclerView.Adapter<RegionalCardAdapter.MyViewHolder>() {

    internal var elections = LiveElections(listOf())

    class MyViewHolder(val card: CardView) : RecyclerView.ViewHolder(card)

    fun updateItems(newItems: LiveElections) {
        val diffCallback = LiveElectionsDiff(elections, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        elections = newItems

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val card = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_regional_election, parent, false) as CardView

        return MyViewHolder(card)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val card = holder.card

        val liveElection = elections.items[position]
        val election = liveElection.election

        card.findViewById<TextView>(R.id.name_card_regional_election).text = election.place
        card.setOnClickListener { onElectionClicked(liveElection) }
        (card.findViewById(R.id.chart_card_regional_election) as PieChart).drawWithResults(election.results)
    }

    override fun getItemCount() = elections.items.size
}
