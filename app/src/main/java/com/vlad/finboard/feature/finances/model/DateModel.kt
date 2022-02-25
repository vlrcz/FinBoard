package com.vlad.finboard.feature.finances.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DateModel(
    val dateString: String,
    val dateMillis: Long
) : FinanceWithDate, Parcelable {

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
