package com.vlad.finboard.feature.finances.detail

import com.vlad.finboard.feature.finances.model.CategoryModel

data class FinancesDetailState(
    val financeId: String,
    val createAt: Long,
    val categoriesList: List<CategoryModel>,
    val selectedCategoryId: Int,
    val type: String,
    val sum: Double
) {
    companion object {
        const val DEFAULT_SUM = 0.0
    }
}