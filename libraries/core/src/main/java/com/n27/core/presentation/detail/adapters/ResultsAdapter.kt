package com.n27.core.presentation.detail.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.n27.core.Constants
import com.n27.core.R
import com.n27.core.domain.election.models.Election
import com.n27.core.domain.election.models.Result
import com.n27.core.extensions.divide
import java.text.NumberFormat

typealias OnResultClicked = (position: Int, result: Result) -> Unit

class ResultsAdapter(
    internal val onResultClicked: OnResultClicked
) : RecyclerView.Adapter<ResultsAdapter.MyViewHolder>() {

    internal var election = Election()

    class MyViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout)

    fun updateItems(newItem: Election) {
        val diffCallback = ResultsDiff(election.results, newItem.results)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        election = newItem

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_activity_detail, parent, false) as ConstraintLayout

        return MyViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val layout = holder.layout
        val result = election.results[position]
        val color = Color.parseColor("#${result.party.color}")
        val votes = NumberFormat.getNumberInstance().format(result.votes)
        val percentage = if (election.chamberName == Constants.KEY_SENATE)
            "- %"
        else
            result.votes.divide(election.validVotes) + " %"

        layout.findViewById<TextView>(R.id.color_list_item_activity_detail).setBackgroundColor(color)
        layout.findViewById<TextView>(R.id.party_list_item_activity_detail).text = result.party.name
        layout.findViewById<TextView>(R.id.votes_list_item_activity_detail).text = votes
        layout.findViewById<TextView>(R.id.percentage_list_item_activity_detail).text = percentage
        layout.findViewById<TextView>(R.id.elects_list_item_activity_detail).text = result.elects.toString()
        layout.setOnClickListener { onResultClicked(position, result) }
    }

    override fun getItemCount() = election.results.size
}
