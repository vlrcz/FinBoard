package com.vlad.finboard.feature.finances.list

import android.content.Context
import android.graphics.Color
import com.vlad.finboard.R
import com.vlad.finboard.core.data.db.models.CategoryEntity
import com.vlad.finboard.core.data.db.models.FinanceEntity
import com.vlad.finboard.core.data.db.models.FinanceWithCategoryEntity
import com.vlad.finboard.feature.finances.model.DateModel
import com.vlad.finboard.feature.finances.model.FinanceModel
import com.vlad.finboard.feature.finances.model.Item
import com.vlad.finboard.feature.finances.model.SumModel
import com.vlad.finboard.millisToDate
import javax.inject.Inject

class FinancesMapper @Inject constructor(
    private val context: Context
) {

    private fun mapFinanceEntityToModel(
        finance: FinanceEntity,
        category: CategoryEntity
    ): FinanceModel {
        val categoryDrawable =
            context.resources.getIdentifier(category.res, "drawable", context.packageName)
        val categoryColor = Color.parseColor(category.color)
        val categoryName = context.getString(
            context.resources.getIdentifier(category.name, "string", context.packageName)
        )
        val sumWithCurrency = finance.sum.toPlainString() + " " + context.getString(R.string.currency)
        val sum = SumModel(finance.sum, sumWithCurrency)
        val createAt = DateModel(finance.createAt.millisToDate(), finance.createAt)
        return FinanceModel(
            id = finance.id,
            categoryId = category.id,
            categoryName = categoryName,
            categoryColor = categoryColor,
            categoryDrawable = categoryDrawable,
            type = finance.type,
            sum = sum,
            createAt = createAt
        )
    }

    fun mapEntitiesToItems(listEntity: List<FinanceWithCategoryEntity>): List<Item> {
        val map = LinkedHashMap<DateModel, MutableList<FinanceModel>>()
        listEntity.forEach {
            val finance = it.financeEntity
            val category = it.categoryEntity
            val financeModel = mapFinanceEntityToModel(finance, category)
            val finances = map.getOrPut(financeModel.createAt) { mutableListOf() }
            finances.add(financeModel)
        }
        return map.flatMap {
            val items = mutableListOf<Item>()
            items.add(it.key)
            items.addAll(it.value)
            items
        }
    }

    fun mapEntitiesToPieChartMap(listEntity: List<FinanceWithCategoryEntity>): Map<Int, Float> {
        val map = mutableMapOf<Int, Float>()
        var total = 0f
        listEntity.forEach {
            total += it.financeEntity.sum.toFloat()
            val categoryColor = Color.parseColor(it.categoryEntity.color)
            map.merge(categoryColor, it.financeEntity.sum.toFloat(), Float::plus)
        }
        return map.mapValues {
            it.value * 360 / total
        }
    }
}
