package com.vlad.finboard.feature.finances.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.vlad.finboard.R
import com.vlad.finboard.databinding.ItemDateBinding
import com.vlad.finboard.databinding.ItemFinanceBinding
import com.vlad.finboard.feature.finances.model.DateModel
import com.vlad.finboard.feature.finances.model.FinanceModel
import com.vlad.finboard.feature.finances.model.FinanceWithDate

class FinanceListAdapter(
    private val onItemClicked: (finance: FinanceModel) -> Unit
) : ListAdapter<FinanceWithDate, ViewHolder>(FinancesDiffUtilCallback()) {

    companion object {
        const val VIEW_TYPE_DATE = R.layout.item_date
        const val VIEW_TYPE_FINANCE = R.layout.item_finance
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is FinanceModel) {
            VIEW_TYPE_FINANCE
        } else {
            VIEW_TYPE_DATE
        }
    }

    class FinancesListViewHolder(
        private val binding: ItemFinanceBinding,
        private val onItemClicked: (finance: FinanceModel) -> Unit
    ) : ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val item =
                    (bindingAdapter as? FinanceListAdapter)?.getItem(bindingAdapterPosition)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_FINANCE -> {
                val binding =
                    ItemFinanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FinancesListViewHolder(binding, onItemClicked)
            }
            else -> {
                val binding =
                    ItemDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DateViewHolder(binding)
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