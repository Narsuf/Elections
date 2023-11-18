package com.n27.regional_live.presentation.regionals.adapters

import androidx.recyclerview.widget.DiffUtil
import com.n27.core.domain.live.models.LiveElections

class LiveElectionsDiff(
    private val oldList: LiveElections,
    private val newList: LiveElections
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.items.size

    override fun getNewListSize(): Int = newList.items.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList.items[oldItemPosition]
        val newItem = newList.items[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList.items[oldItemPosition]
        val newItem = newList.items[newItemPosition]
        return oldItem == newItem
    }
}