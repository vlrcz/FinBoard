package com.vlad.finboard.presentation.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vlad.finboard.data.db.models.CategoryModel
import com.vlad.finboard.databinding.ItemCategoryBinding
import com.vlad.finboard.presentation.categories.CategoryListAdapter.CategoryListViewHolder
import timber.log.Timber

class CategoryListAdapter(
    private val onItemClicked: (category: CategoryModel) -> Unit
) :
    ListAdapter<CategoryModel, CategoryListViewHolder>(CategoryDiffUtilCallback()) {

    class CategoryListViewHolder(
        private val binding: ItemCategoryBinding,
        private val onItemClicked: (category: CategoryModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.categoryImg.setOnClickListener {
                val item =
                    (bindingAdapter as? CategoryListAdapter)?.getItem(bindingAdapterPosition)
                        ?: return@setOnClickListener

                onItemClicked.invoke(item)
            }
        }

        fun bind(category: CategoryModel) {
            Glide.with(itemView.context)
                .load(category.drawable)
                .into(binding.categoryImg)

            if (category.isSelected) {
                binding.categoryImg.setBackgroundColor(category.color)
            } else {
                binding.categoryImg.background = null
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryListViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CategoryDiffUtilCallback : DiffUtil.ItemCallback<CategoryModel>() {
        override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
            return oldItem == newItem
        }
    }
}