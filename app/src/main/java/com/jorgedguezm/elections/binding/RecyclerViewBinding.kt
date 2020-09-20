package com.jorgedguezm.elections.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

import com.jorgedguezm.elections.extension.bindResource
import com.jorgedguezm.elections.models.Resource
import com.jorgedguezm.elections.models.entities.Election
import com.jorgedguezm.elections.view.adapters.GeneralCardAdapter

@BindingAdapter("adapterElections")
fun bindAdapterElections(view: RecyclerView, resource: Resource<List<Election>>?) {
    view.bindResource(resource) { elections ->
        val adapter = view.adapter as? GeneralCardAdapter

        elections?.sortedWith(compareByDescending { it.date })?.let { sortedElections ->
            adapter?.congressElections = sortedElections.filter { it.chamberName == "Congreso" }
            adapter?.senateElections = sortedElections.filter { it.chamberName == "Senado" }
            adapter?.notifyDataSetChanged()
        }
    }
}