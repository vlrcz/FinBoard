package com.vlad.finboard.feature.categories.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.vlad.finboard.R
import com.vlad.finboard.feature.finances.model.CategoryModel

class CategoriesListAdapter(
    private val onItemClicked: (category: CategoryModel) -> Unit
) : ListAdapter<CategoryModel, CategoriesListViewHolder>(CategoriesDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoriesListViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: CategoriesListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: CategoriesListViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            holder.bind(getItem(position), payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }
}