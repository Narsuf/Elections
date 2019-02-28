package com.jorgedguezm.elections.ui

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

    var parties = HashMap<String, String>()
    var results = ArrayList<List<Results>>()

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val card: CardView) : RecyclerView.ViewHolder(card)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): GeneralCardAdapter.MyViewHolder {
        // create a new view
        val card = LayoutInflater.from(parent.context)
                .inflate(R.layout.general_elections_card, parent, false) as CardView
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(card)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val concatenatedText = context.resources.getString(R.string.app_name) + " " +
                elections[position].year

        holder.card.section_label.text = concatenatedText
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = elections.size
}