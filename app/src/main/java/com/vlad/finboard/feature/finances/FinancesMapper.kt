package com.vlad.finboard.feature.finances

import android.content.Context
import android.graphics.Color
import com.vlad.finboard.core.data.db.models.CategoryEntity
import com.vlad.finboard.core.data.db.models.FinanceEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FinancesMapper @Inject constructor(
    private val context: Context
) {

    fun mapFinanceEntityToModel(finance: FinanceEntity, category: CategoryEntity): FinanceModel {
        val categoryDrawable =
            context.resources.getIdentifier(category.res, "drawable", context.packageName)
        val categoryColor = Color.parseColor(category.color)
        val categoryName =
            context.getString(
                context.resources.getIdentifier(
                    category.name,
                    "string",
                    context.packageName
                )
            )
        return FinanceModel(
            id = finance.id,
            categoryId = category.id,
            categoryName = categoryName,
            categoryColor = categoryColor,
            categoryDrawable = categoryDrawable,
            categoryType = category.type,
            sum = finance.sum,
            date = finance.date
        )
    }
}
