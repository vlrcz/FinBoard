package com.vlad.finboard.feature.finances.list

import com.vlad.finboard.core.data.db.FinancesDao
import com.vlad.finboard.core.data.db.models.FinanceEntity
import com.vlad.finboard.core.data.db.models.FinanceWithCategoryEntity
import javax.inject.Inject

class FinancesRepository @Inject constructor(
    private val financesDao: FinancesDao
) {

    suspend fun saveFinance(finance: FinanceEntity) {
        financesDao.insertFinance(finance)
    }

    suspend fun fetchFinancesWithCategoryByType(type: String): List<FinanceWithCategoryEntity> {
        return financesDao.fetchFinancesWithCategoryByType(type)
    }
}