package com.vlad.finboard.feature.finances.adapter

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.vlad.finboard.feature.finances.model.DateModel
import com.vlad.finboard.feature.finances.model.FinanceModel
import com.vlad.finboard.feature.finances.model.Item

class FinancesDiffUtilCallback : ItemCallback<Item>() {

    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return when {
            oldItem is DateModel && newItem is DateModel -> {
                oldItem == newItem
            }
            oldItem is FinanceModel && newItem is FinanceModel -> {
                oldItem == newItem
            }
            else -> {
                false
            }
        }
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return when {
            oldItem is DateModel && newItem is DateModel -> {
                oldItem.dateMillis == newItem.dateMillis
            }
            oldItem is FinanceModel && newItem is FinanceModel -> {
                oldItem.id == newItem.id
            }
            else -> {
                false
            }
        }
    }
}