package com.vlad.finboard.feature.finances.model

import android.os.Parcelable
import java.math.BigDecimal
import kotlinx.parcelize.Parcelize

@Parcelize
data class SumModel(
    val sumBigDecimal: BigDecimal,
    val sumWithCurrency: String
) : Parcelable
