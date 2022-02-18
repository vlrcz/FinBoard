package com.vlad.finboard.data.db.models

import android.content.Context
import android.graphics.Color
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryMapper @Inject constructor(
    private val context: Context
) {

    fun mapEntityToModel(category: CategoryEntity): CategoryModel {
        val color = Color.parseColor(category.color)
        val drawable =
            context.resources.getIdentifier(category.res, "drawable", context.packageName)
        return CategoryModel(
            category.id,
            category.name,
            category.type,
            color,
            drawable
        )
    }
}