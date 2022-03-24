package com.vlad.finboard.feature.categories.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.vlad.finboard.databinding.ItemCategoryBinding
import com.vlad.finboard.feature.finances.model.CategoryModel

class CategoriesListViewHolder(
    view: View,
    private val onItemClicked: (category: CategoryModel) -> Unit
) : ViewHolder(view) {

    private val binding: ItemCategoryBinding = ItemCategoryBinding.bind(view)

    init {
        binding.categoryImg.setOnClickListener {
            val item =
                (bindingAdapter as? CategoriesListAdapter)?.currentList?.get(bindingAdapterPosition)
                    ?: return@setOnClickListener

            onItemClicked.invoke(item)
        }
    }

    fun bind(category: CategoryModel) {
        Glide.with(itemView.context)
            .load(category.drawable)
            .into(binding.categoryImg)

        bindBackground(category)
        binding.categoryTxt.text = category.name
    }

    private fun bindBackground(category: CategoryModel) {
        if (category.isSelected) {
            binding.categoryImg.setBackgroundColor(category.color)
        } else {
            binding.categoryImg.background = null
        }
    }

    fun bind(category: CategoryModel, payload: List<Any>) {

    }
}