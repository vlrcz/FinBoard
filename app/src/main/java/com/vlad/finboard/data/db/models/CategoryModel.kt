package com.vlad.finboard.data.db.models

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

data class CategoryModel(
    val id: Int,
    val name: String,
    val type: String,
    @ColorInt
    val color: Int,
    @DrawableRes
    val drawable: Int
)