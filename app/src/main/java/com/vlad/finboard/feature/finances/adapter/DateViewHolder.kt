package com.vlad.finboard.feature.finances.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.vlad.finboard.databinding.ItemDateBinding
import com.vlad.finboard.databinding.ItemFinanceBinding
import com.vlad.finboard.feature.finances.model.DateModel

class DateViewHolder(
    view: View
) : ViewHolder(view) {

    private val binding: ItemDateBinding = ItemDateBinding.bind(view)

    fun bind(dateModel: DateModel) {
        binding.dateFinanceTxt.text = dateModel.dateString
    }
}