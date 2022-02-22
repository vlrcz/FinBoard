package com.vlad.finboard.feature.finances

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.vlad.finboard.databinding.ItemDateBinding
import com.vlad.finboard.databinding.ItemFinanceBinding

class FinanceListAdapter(
    private val onItemClicked: (finance: FinanceModel) -> Unit
) :
    ListAdapter<FinanceModel, ViewHolder>(FinancesDiffUtilCallback()) {

    companion object {
        const val VIEW_TYPE_DATE = 1
        const val VIEW_TYPE_FINANCE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isDate) {
            VIEW_TYPE_DATE
        } else {
            VIEW_TYPE_FINANCE
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

                onItemClicked.invoke(item)
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

    class DateViewHolder(
        private val binding: ItemDateBinding
    ) : ViewHolder(binding.root) {

        fun bind(finance: FinanceModel) {
            binding.dateFinanceTxt.text = finance.date
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
                holder.bind(getItem(position))
            }
            is DateViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    class FinancesDiffUtilCallback : DiffUtil.ItemCallback<FinanceModel>() {
        override fun areItemsTheSame(oldItem: FinanceModel, newItem: FinanceModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FinanceModel, newItem: FinanceModel): Boolean {
            return oldItem == newItem
        }
    }
}