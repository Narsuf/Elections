package com.n27.regional_live.ui.regional_live.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.n27.core.data.models.Election
import com.n27.core.extensions.drawWithResults
import com.n27.core.presentation.common.OnElectionClicked
import com.n27.regional_live.R

class RegionalCardAdapter(
    private val elections: List<Election>,
    private val onElectionClicked: OnElectionClicked
) : RecyclerView.Adapter<RegionalCardAdapter.MyViewHolder>() {

    class MyViewHolder(val card: CardView) : RecyclerView.ViewHolder(card)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val card = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_regional_election, parent, false) as CardView

        return MyViewHolder(card)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val card = holder.card

        val election = elections[position]

        card.findViewById<TextView>(R.id.card_region_name).text = election.place
        card.setOnClickListener { onElectionClicked(election) }

        (card.findViewById(R.id.pie_chart) as PieChart).drawWithResults(election.results)

        if (position == 0) {
            card.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = card.context.resources.getDimensionPixelSize(R.dimen.default_spacing)
            }
        }
    }

    override fun getItemCount() = elections.size
}
