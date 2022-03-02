package com.vlad.finboard.feature.finances.model

import android.os.Parcelable
import com.vlad.finboard.R
import java.util.Date
import kotlinx.parcelize.Parcelize

@Parcelize
data class DateModel(
    val dateString: String,
    val dateMillis: Long
) : Item, Parcelable {

    override fun getItemViewType(): Int {
        return R.layout.item_date
    }

    override fun areItemsTheSame(item: Item): Boolean {
        if (item !is DateModel) return false

        return dateString == item.dateString
    }

    override fun areContentsTheSame(item: Item): Boolean {
        if (item !is DateModel) return false

        return this == item
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DateModel

        if (dateString != other.dateString) return false

        return true
    }

    override fun hashCode(): Int {
        return dateString.hashCode()
    }
}
