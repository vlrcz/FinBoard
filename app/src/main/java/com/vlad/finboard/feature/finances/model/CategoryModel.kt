package com.vlad.finboard.feature.finances.model

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

data class CategoryModel(
    val id: Int,
    val name: String,
    val type: String,
    @ColorInt
    val color: Int,
    @DrawableRes
    val drawable: Int,
    var isSelected: Boolean = false
)