package com.n27.core.presentation.detail.dialog.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.n27.core.R
import com.n27.core.presentation.detail.dialog.models.Info


class InfoAdapter(
    private val infoList: List<Info>,
    private val names: Array<String>
) : RecyclerView.Adapter<InfoAdapter.MyViewHolder>() {

    class MyViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_dialog_detail, parent, false) as ConstraintLayout

        return MyViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val layout = holder.layout
        val info = infoList[position]
        val name = names[position]

        layout.findViewById<TextView>(R.id.text_list_item_dialog_detail).text = name
        layout.findViewById<TextView>(R.id.number_list_item_dialog_detail).text = info.value
        info.percentage?.let {
            val percentage = "$it %"
            layout.findViewById<TextView>(R.id.percentage_list_item_dialog_detail).text = percentage
        }
    }

    override fun getItemCount() = infoList.size
}

