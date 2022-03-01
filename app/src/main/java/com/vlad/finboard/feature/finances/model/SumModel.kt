package com.vlad.finboard.feature.finances.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SumModel(
    val sumDouble: Double,
    val sumWithCurrency: String
) : Parcelable
