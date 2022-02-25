package com.vlad.finboard.feature.finances.adapter

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.vlad.finboard.feature.finances.model.DateModel
import com.vlad.finboard.feature.finances.model.FinanceModel
import com.vlad.finboard.feature.finances.model.FinanceWithDate

class FinancesDiffUtilCallback : ItemCallback<FinanceWithDate>() {

    override fun areItemsTheSame(oldItem: FinanceWithDate, newItem: FinanceWithDate): Boolean {
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

    override fun areContentsTheSame(oldItem: FinanceWithDate, newItem: FinanceWithDate): Boolean {
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