package com.jorgedguezm.elections.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_CALLED_FROM
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_CONGRESS_ELECTIONS
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_CONGRESS_RESULTS
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_GENERAL
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_PARTIES
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_SENATE_ELECTIONS
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_SENATE_RESULTS
import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.Results
import com.jorgedguezm.elections.ui.ElectionsViewModel
import com.jorgedguezm.elections.ui.detail.DetailActivity
import com.jorgedguezm.elections.ui.main.PlaceholderFragment
import com.jorgedguezm.elections.utils.Utils

import kotlinx.android.synthetic.main.general_elections_card.view.*

import javax.inject.Inject

class GeneralCardAdapter @Inject constructor(private val context: Context,
                                             var congressElections: Array<Election>,
                                             val utils: Utils) :
        RecyclerView.Adapter<GeneralCardAdapter.MyViewHolder>() {

    var partiesColor = HashMap<String, String>()
    var congressResults = ArrayList<List<Results>>()

    lateinit var senateElections: Array<Election>

    lateinit var fragment: PlaceholderFragment
    lateinit var electionsViewModel: ElectionsViewModel

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

        return MyViewHolder(card)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val congressResult = congressResults[position]
        val congressElection = congressElections[position]
        val senateElection = senateElections[position]
        val concatenatedText = context.resources.getString(R.string.app_name) + " " +
                congressElection.year

        holder.card.section_label.text = concatenatedText
        holder.card.setOnClickListener {
            val myIntent = Intent(fragment.context, DetailActivity::class.java)
            myIntent.putExtra(KEY_CALLED_FROM, KEY_GENERAL)
            myIntent.putExtra(KEY_PARTIES, partiesColor)
            myIntent.putExtra(KEY_CONGRESS_ELECTIONS, congressElection)
            myIntent.putExtra(KEY_CONGRESS_RESULTS, ArrayList(congressResult))
            myIntent.putExtra(KEY_SENATE_ELECTIONS, senateElection)
            loadSenateResultsAndCallIntent(myIntent, senateElection)
        }

        utils.drawPieChart(holder.card.pie_chart, utils.getElectsFromResults(congressResult),
                utils.getColorsFromResults(congressResult, partiesColor))
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = congressElections.size

    private fun loadSenateResultsAndCallIntent(intent: Intent, election: Election) {
        electionsViewModel.loadResults(election.year, election.place,
                election.chamberName!!)

        electionsViewModel.resultsResult().observe(fragment,
                Observer<List<Results>> {
                    intent.putExtra(KEY_SENATE_RESULTS, ArrayList(it))
                    fragment.startActivity(intent)
                })
    }
}