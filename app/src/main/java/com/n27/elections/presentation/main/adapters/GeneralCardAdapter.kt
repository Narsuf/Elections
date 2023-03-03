package com.n27.elections.presentation.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.n27.core.data.models.Election
import com.n27.core.presentation.common.PresentationUtils
import com.n27.elections.R
import com.n27.elections.presentation.main.entities.OnElectionClicked
import javax.inject.Inject

class GeneralCardAdapter @Inject constructor(private val utils: PresentationUtils) :
        RecyclerView.Adapter<GeneralCardAdapter.MyViewHolder>() {

    var elections: List<Election> = ArrayList()
        set(value) {
            field = value
            congressElections = elections.filter { it.chamberName == "Congreso" }
            senateElections = elections.filter { it.chamberName == "Senado" }
        }

    var onElectionClicked: OnElectionClicked = { _, _ -> }

    private var congressElections: List<Election> = ArrayList()
    private var senateElections: List<Election> = ArrayList()

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
        val concatenatedText = congressElection.date
        val card = holder.card

        card.findViewById<TextView>(R.id.section_label).text = concatenatedText
        card.setOnClickListener { onElectionClicked(congressElection, senateElection) }

        utils.drawPieChart(card.findViewById(R.id.pie_chart), congressElection.results)

        if (position == 0) {
            card.updateLayoutParams<MarginLayoutParams> {
                topMargin = card.context.resources.getDimensionPixelSize(R.dimen.default_spacing)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = congressElections.size
}
