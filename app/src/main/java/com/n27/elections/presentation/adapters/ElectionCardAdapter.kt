package com.n27.elections.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.n27.core.domain.election.Election
import com.n27.core.extensions.drawWithResults
import com.n27.elections.R

typealias OnElectionClicked = (congressElection: Election, senateElection: Election) -> Unit

class ElectionCardAdapter(
    internal val onElectionClicked: OnElectionClicked
) : RecyclerView.Adapter<ElectionCardAdapter.MyViewHolder>() {

    internal var congressElections = listOf<Election>()
    internal var senateElections = listOf<Election>()

    class MyViewHolder(val card: CardView) : RecyclerView.ViewHolder(card)

    fun updateItems(newCongressElections: List<Election>, newSenateElections: List<Election>) {
        val diffCallback = ElectionsDiff(congressElections, newCongressElections)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        congressElections = newCongressElections
        senateElections = newSenateElections

        diffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val card = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_general_elections, parent, false) as CardView

        return MyViewHolder(card)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val congressElection = congressElections[position]
        val senateElection = senateElections[position]
        val card = holder.card

        card.findViewById<TextView>(R.id.year_card_general_elections).text = congressElection.date
        card.setOnClickListener { onElectionClicked(congressElection, senateElection) }
        (card.findViewById(R.id.pie_chart_card_general_elections) as PieChart).drawWithResults(congressElection.results)
    }

    override fun getItemCount() = congressElections.size
}
