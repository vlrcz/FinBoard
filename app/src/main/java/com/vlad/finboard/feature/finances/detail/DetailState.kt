package com.vlad.finboard.feature.finances.detail

import com.vlad.finboard.feature.finances.model.CategoryModel

data class DetailState(
    val categoriesList: List<CategoryModel>,
    val selectedCategoryId: Int,
    val financeId: String,
    val sum: Double,
    val type: String,
    val createAt: Long
)