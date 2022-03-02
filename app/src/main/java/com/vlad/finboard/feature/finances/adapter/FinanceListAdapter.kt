package com.vlad.finboard.feature.finances.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.vlad.finboard.R
import com.vlad.finboard.databinding.ItemDateBinding
import com.vlad.finboard.databinding.ItemFinanceBinding
import com.vlad.finboard.feature.finances.model.DateModel
import com.vlad.finboard.feature.finances.model.FinanceModel
import com.vlad.finboard.feature.finances.model.Item

class FinanceListAdapter(
    private val onItemClicked: (finance: FinanceModel) -> Unit
) : ListAdapter<Item, ViewHolder>(FinancesDiffUtilCallback()) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getItemViewType()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            R.layout.item_finance -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_finance, parent, false)
                FinancesListViewHolder(view, onItemClicked)
            }
            R.layout.item_date -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_date, parent, false)
                DateViewHolder(view)
            }
            else -> {
                throw IllegalArgumentException("Adapter item viewType not found")
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is FinancesListViewHolder -> {
                holder.bind(getItem(position) as FinanceModel)
            }
            is DateViewHolder -> {
                holder.bind(getItem(position) as DateModel)
            }
        }
    }
}