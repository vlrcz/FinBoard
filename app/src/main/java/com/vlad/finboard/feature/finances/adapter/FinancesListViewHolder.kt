package com.vlad.finboard.feature.finances.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.vlad.finboard.databinding.ItemFinanceBinding
import com.vlad.finboard.feature.finances.model.FinanceModel

class FinancesListViewHolder(
    view: View,
    private val onItemClicked: (finance: FinanceModel) -> Unit
) : ViewHolder(view) {

    private val binding: ItemFinanceBinding = ItemFinanceBinding.bind(view)

    init {
        binding.root.setOnClickListener {
            val item =
                (bindingAdapter as? FinanceListAdapter)?.currentList?.get(bindingAdapterPosition)
                    ?: return@setOnClickListener

            onItemClicked.invoke(item as FinanceModel)
        }
    }

    fun bind(finance: FinanceModel) {
        binding.financeSumTxt.text = finance.sum.sumWithCurrency
        binding.financeNameTxt.text = finance.categoryName
        binding.financeImg.setBackgroundColor(finance.categoryColor)
        Glide.with(itemView.context)
            .load(finance.categoryDrawable)
            .into(binding.financeImg)
    }
}