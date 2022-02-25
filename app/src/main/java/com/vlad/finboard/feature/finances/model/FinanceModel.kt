package com.vlad.finboard.feature.finances.model

import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
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
    val sum: Double,
    val createAt: DateModel,
    val updateAt: DateModel
) : Parcelable, FinanceWithDate {

    fun sumWithRub(): String {
        return "$sum руб."
    }
}