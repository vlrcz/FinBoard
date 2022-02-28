package com.vlad.finboard.feature.finances.list

import android.content.Context
import android.graphics.Color
import com.vlad.finboard.R
import com.vlad.finboard.core.data.db.models.CategoryEntity
import com.vlad.finboard.core.data.db.models.FinanceEntity
import com.vlad.finboard.feature.finances.model.DateModel
import com.vlad.finboard.feature.finances.model.FinanceModel
import com.vlad.finboard.millisToDate
import javax.inject.Inject

class FinancesMapper @Inject constructor(
    private val context: Context
) {

    fun mapFinanceEntityToModel(finance: FinanceEntity, category: CategoryEntity): FinanceModel {
        val categoryDrawable =
            context.resources.getIdentifier(category.res, "drawable", context.packageName)
        val categoryColor = Color.parseColor(category.color)
        val categoryName = context.getString(
            context.resources.getIdentifier(category.name, "string", context.packageName)
        )
        val financeSum = finance.sum.toString() + " " + context.getString(R.string.currency)
        val createAt = DateModel(finance.createAt.millisToDate(), finance.createAt)
        val updateAt = DateModel(finance.updateAt.millisToDate(), finance.updateAt)
        return FinanceModel(
            id = finance.id,
            categoryId = category.id,
            categoryName = categoryName,
            categoryColor = categoryColor,
            categoryDrawable = categoryDrawable,
            categoryType = category.type,
            sum = financeSum,
            createAt = createAt,
            updateAt = updateAt
        )
    }
}
