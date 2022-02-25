package com.vlad.finboard.feature.finances.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.vlad.finboard.databinding.ItemDateBinding
import com.vlad.finboard.feature.finances.model.DateModel

class DateViewHolder(
    private val binding: ItemDateBinding
) : ViewHolder(binding.root) {

    fun bind(dateModel: DateModel) {
        binding.dateFinanceTxt.text = dateModel.dateString
    }
}