package com.n27.elections.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.n27.core.domain.election.models.Election
import com.n27.core.extensions.drawWithResults
import com.n27.elections.R

typealias OnGeneralElectionClicked = (congressElection: Election, senateElection: Election) -> Unit

class GeneralElectionsCardAdapter(
    internal val congressElections: List<Election>,
    internal val senateElections: List<Election>,
    internal val onElectionClicked: OnGeneralElectionClicked
) : RecyclerView.Adapter<GeneralElectionsCardAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val card: CardView) : RecyclerView.ViewHolder(card)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // create a new view
        val card = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_general_elections, parent, false) as CardView

        return MyViewHolder(card)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val congressElection = congressElections[position]
        val senateElection = senateElections[position]
        val card = holder.card

        card.findViewById<TextView>(R.id.year_card_general_elections).text = congressElection.date
        card.setOnClickListener { onElectionClicked(congressElection, senateElection) }
        (card.findViewById(R.id.pie_chart_card_general_elections) as PieChart).drawWithResults(congressElection.results)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = congressElections.size
}
