package com.vlad.finboard.feature.finances

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

data class FinanceModel(
    val id: String,
    val categoryName: String,
    @ColorInt
    val categoryColor: Int,
    @DrawableRes
    val categoryDrawable: Int,
    val categoryType: String,
    val sum: String,
    val date: String,
    val isDate: Boolean = false
) {
    fun sumWithRub(): String {
        return "$sum руб."
    }
}