package com.vlad.finboard.feature.finances

import com.vlad.finboard.core.data.db.FinancesDao
import com.vlad.finboard.core.data.db.models.CategoryEntity
import com.vlad.finboard.core.data.db.models.FinanceEntity
import javax.inject.Inject

class FinancesRepository @Inject constructor(
    private val financesDao: FinancesDao
) {

    suspend fun saveFinance(finance: FinanceEntity) {
        financesDao.insertFinance(finance)
    }

    suspend fun fetchFinances(type: String): Map<FinanceEntity, CategoryEntity> {
        return financesDao.fetchFinances(type)
    }
}