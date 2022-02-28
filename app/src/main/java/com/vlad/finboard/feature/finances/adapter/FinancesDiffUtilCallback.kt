package com.vlad.finboard.feature.finances.adapter

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.vlad.finboard.feature.finances.model.Item

class FinancesDiffUtilCallback : ItemCallback<Item>() {

    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }
}