package com.jorgedguezm.elections.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.Constants.KEY_CALLED_FROM
import com.jorgedguezm.elections.Constants.KEY_CONGRESS_ELECTION
import com.jorgedguezm.elections.Constants.KEY_GENERAL
import com.jorgedguezm.elections.Constants.KEY_SENATE_ELECTION
import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.ui.detail.DetailActivity
import com.jorgedguezm.elections.Utils
import com.jorgedguezm.elections.ui.main.PlaceholderFragment

import kotlinx.android.synthetic.main.general_elections_card.view.*

import javax.inject.Inject

class GeneralCardAdapter @Inject constructor(
        val utils: Utils) : RecyclerView.Adapter<GeneralCardAdapter.MyViewHolder>() {

    var congressElections: List<Election> = ArrayList()
    var senateElections: List<Election> = ArrayList()

    lateinit var fragment: PlaceholderFragment

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val card: CardView) : RecyclerView.ViewHolder(card)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // create a new view
        val card = LayoutInflater.from(parent.context)
                .inflate(R.layout.general_elections_card, parent, false) as CardView

        return MyViewHolder(card)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val congressElection = congressElections[position]
        val senateElection = senateElections[position]
        val concatenatedText = fragment.resources.getString(R.string.app_name) + " " +
                congressElection.date

        holder.card.section_label.text = concatenatedText
        holder.card.setOnClickListener {
            val myIntent = Intent(fragment.context, DetailActivity::class.java)
            myIntent.putExtra(KEY_CONGRESS_ELECTION, congressElection)
            myIntent.putExtra(KEY_SENATE_ELECTION, senateElection)
            fragment.startActivity(myIntent)
        }

        utils.drawPieChart(holder.card.pie_chart, congressElection.results)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = congressElections.size
}