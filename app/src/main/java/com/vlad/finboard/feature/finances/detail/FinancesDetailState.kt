package com.vlad.finboard.feature.finances.detail

import com.vlad.finboard.feature.finances.model.CategoryModel

data class FinancesDetailState(
    val categoriesList: List<CategoryModel>,
    val selectedCategoryId: Int
)