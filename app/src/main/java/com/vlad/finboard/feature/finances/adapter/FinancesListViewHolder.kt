package com.vlad.finboard.feature.finances.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.vlad.finboard.databinding.ItemFinanceBinding
import com.vlad.finboard.feature.finances.model.FinanceModel

class FinancesListViewHolder(
    private val binding: ItemFinanceBinding,
    private val onItemClicked: (finance: FinanceModel) -> Unit
) : ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            val item =
                (bindingAdapter as? FinanceListAdapter)?.currentList?.get(bindingAdapterPosition)
                    ?: return@setOnClickListener

            onItemClicked.invoke(item as FinanceModel)
        }
    }

    fun bind(finance: FinanceModel) {
        binding.financeSumTxt.text = finance.sumWithRub()
        binding.financeNameTxt.text = finance.categoryName
        binding.financeImg.setBackgroundColor(finance.categoryColor)
        Glide.with(itemView.context)
            .load(finance.categoryDrawable)
            .into(binding.financeImg)
    }
}