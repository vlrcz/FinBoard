package com.vlad.finboard.feature.finances.model

import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.vlad.finboard.R
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
    val createAt: DateModel,
    val updateAt: DateModel
) : Parcelable, Item {

    override fun getItemViewType(): Int {
        return R.layout.item_finance
    }

    override fun areItemsTheSame(item: Item): Boolean {
        if (item !is FinanceModel) return false

        return id == item.id
    }

    override fun areContentsTheSame(item: Item): Boolean {
        if (item !is FinanceModel) return false

        return this == item
    }
}