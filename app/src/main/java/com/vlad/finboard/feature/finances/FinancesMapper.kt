package com.vlad.finboard.feature.finances

import android.content.Context
import android.graphics.Color
import com.vlad.finboard.core.data.db.models.CategoryEntity
import com.vlad.finboard.core.data.db.models.CategoryName
import com.vlad.finboard.core.data.db.models.FinanceEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FinancesMapper @Inject constructor(
    private val context: Context
) {

    fun mapNoteEntityToModel(entity: FinanceEntity, category: CategoryEntity): FinanceModel {
        val categoryDrawable =
            context.resources.getIdentifier(category.res, "drawable", context.packageName)
        val categoryColor = Color.parseColor(category.color)
        val categoryName = CategoryName.valueOf(category.name)
        return FinanceModel(
            id = entity.id,
            categoryName = categoryName.toString(),
            categoryColor = categoryColor,
            categoryDrawable = categoryDrawable,
            categoryType = category.type,
            sum = entity.sum,
            date = entity.date
        )
    }
}
