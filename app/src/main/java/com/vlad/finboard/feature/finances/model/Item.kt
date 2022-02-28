package com.vlad.finboard.feature.finances.model

interface Item {
    fun getItemViewType(): Int
    fun areItemsTheSame(item: Item): Boolean
    fun areContentsTheSame(item: Item): Boolean
}
