package com.jorgedguezm.elections.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.Results
import com.jorgedguezm.elections.utils.Utils

import kotlinx.android.synthetic.main.general_elections_card.view.*

import javax.inject.Inject

class GeneralCardAdapter @Inject constructor(private val context: Context,
                                             var elections: Array<Election>, val utils: Utils):
        RecyclerView.Adapter<GeneralCardAdapter.MyViewHolder>() {

    var partiesColor = HashMap<String, String>()
    var results = ArrayList<List<Results>>()

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val card: CardView) : RecyclerView.ViewHolder(card)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        // create a new view
        val card = LayoutInflater.from(parent.context)
                .inflate(R.layout.general_elections_card, parent, false) as CardView

        card.setOnClickListener {

        }

        return MyViewHolder(card)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val concatenatedText = context.resources.getString(R.string.app_name) + " " +
                elections[position].year

        holder.card.section_label.text = concatenatedText

        if (results.size > 0)
            utils.drawPieChart(holder.card.pie_chart, getElects(position), getColors(position))
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = elections.size

    private fun getElects(position: Int): Array<Int> {
        val elects = ArrayList<Int>()

        for (r in results[position])
            elects.add(r.elects!!)

        return elects.toTypedArray()
    }

    private fun getColors(position: Int): Array<String> {
        val colors = ArrayList<String>()

        for (r in results[position])
            colors.add("#" + partiesColor[r.partyId]!!)

        return colors.toTypedArray()
    }
}