package com.vlad.finboard.feature.categories.adapter

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.vlad.finboard.feature.finances.model.CategoryModel

class CategoriesDiffUtilCallback : ItemCallback<CategoryModel>() {
    override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: CategoryModel, newItem: CategoryModel): Any? {

        return super.getChangePayload(oldItem, newItem)
    }
}