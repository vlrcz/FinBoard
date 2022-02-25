package com.vlad.finboard.feature.finances

import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class FinanceModel(
    val id: String,
    val categoryId: Int,
    val categoryName: String,
    @ColorInt
    val categoryColor: Int,
    @DrawableRes
    val categoryDrawable: Int,
    val categoryType: String,
    val sum: String,
    val date: String,
    val isDate: Boolean = false
) : Parcelable {
    fun sumWithRub(): String {
        return "$sum руб."
    }
}