package com.n27.regional_live.presentation.locals.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.n27.core.domain.live.models.Region
import com.n27.regional_live.R

typealias OnRegionClicked = (region: Region) -> Unit

class LocalsCardAdapter(
    private val onRegionClicked: OnRegionClicked
) : RecyclerView.Adapter<LocalsCardAdapter.MyViewHolder>() {

    internal var regions = listOf<Region>()

    class MyViewHolder(val card: CardView) : RecyclerView.ViewHolder(card)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val card = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_local_election, parent, false) as CardView

        return MyViewHolder(card)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val card = holder.card

        val region = regions[position]

        card.findViewById<TextView>(R.id.region_name_card_local_election).text = region.name
        card.setOnClickListener { onRegionClicked(region) }
    }

    override fun getItemCount() = regions.size
}
