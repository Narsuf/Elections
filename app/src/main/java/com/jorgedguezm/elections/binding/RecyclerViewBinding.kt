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

        elections?.sortedByDescending {
            if (it.date.length > 4)
                it.date.toDouble() / 10
            else
                it.date.toDouble()
        }?.let { sortedElections ->
            sortedElections.forEach {
                if (it.date.length > 4) {
                    it.date = when (it.date) {
                        "20192" -> "2019-10N"
                        "20191" -> "2019-28A"
                        else -> it.date
                    }
                }
            }

            adapter?.congressElections = sortedElections.filter { it.chamberName == "Congreso" }
            adapter?.senateElections = sortedElections.filter { it.chamberName == "Senado" }
            adapter?.notifyDataSetChanged()
        }
    }
}