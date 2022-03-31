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

    suspend fun updateFinance(finance: FinanceEntity) {
        financesDao.updateFinance(finance)
    }

    suspend fun fetchLimitedFinancesWithCategoryByType(type: String, limit: Int, offset: Int): List<FinanceWithCategoryEntity> {
        return financesDao.fetchLimitedFinancesWithCategoryByType(type, limit, offset)
    }

    suspend fun fetchAllFinancesWithCategoryByType(type: String): List<FinanceWithCategoryEntity> {
        return financesDao.fetchAllFinancesWithCategoryByType(type)
    }
}