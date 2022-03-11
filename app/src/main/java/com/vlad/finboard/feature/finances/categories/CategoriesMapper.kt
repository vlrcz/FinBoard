package com.vlad.finboard.feature.finances.categories

import android.content.Context
import android.graphics.Color
import com.vlad.finboard.core.data.db.models.CategoryEntity
import com.vlad.finboard.feature.finances.model.CategoryModel
import javax.inject.Inject

class CategoriesMapper @Inject constructor(
    private val context: Context
) {

    fun mapEntityToModel(category: CategoryEntity): CategoryModel {
        val color = Color.parseColor(category.color)
        val drawable =
            context.resources.getIdentifier(category.res, "drawable", context.packageName)
        return CategoryModel(
            id = category.id,
            name = category.name,
            type = category.type,
            color = color,
            drawable = drawable
        )
    }
}