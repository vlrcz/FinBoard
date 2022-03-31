package com.vlad.finboard.feature.finances.list

import android.content.Context
import android.graphics.Color
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import com.vlad.finboard.R
import com.vlad.finboard.core.data.db.models.CategoryEntity
import com.vlad.finboard.core.data.db.models.FinanceEntity
import com.vlad.finboard.core.data.db.models.FinanceWithCategoryEntity
import com.vlad.finboard.feature.charts.PieChartView
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

    data class FinancesPage(
        val itemsList: List<Item>,
        val pieChartMap: Map<Int,Float>
    )

    fun mapEntities(listEntity: List<FinanceWithCategoryEntity>): FinancesPage {
        val map = LinkedHashMap<DateModel, MutableList<FinanceModel>>()
        val models = mutableListOf<FinanceModel>()
        listEntity.forEach {
            val finance = it.financeEntity
            val category = it.categoryEntity
            val financeModel = mapFinanceEntityToModel(finance, category)
            models.add(financeModel)
            val finances = map.getOrPut(financeModel.createAt) { mutableListOf() }
            finances.add(financeModel)
        }
        val itemsList = map.flatMap {
            val items = mutableListOf<Item>()
            items.add(it.key)
            items.addAll(it.value)
            items
        }
        val pieChartMap = mapFinanceModelsToPieChartMap(models)
        return FinancesPage(itemsList, pieChartMap)
    }

    private fun mapFinanceModelsToPieChartMap(list: List<FinanceModel>): Map<Int, Float> {
        val map = mutableMapOf<Int, Float>()
        var total = 0f
        list.forEach {
            total += it.sum.sumBigDecimal.toFloat()
            map.merge(it.categoryColor, it.sum.sumBigDecimal.toFloat(), Float::plus)
        }
        return map.mapValues {
            it.value * 360 / total
        }
    }
}
