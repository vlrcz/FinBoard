package com.vlad.finboard.feature.finances

import com.vlad.finboard.core.data.db.CategoriesDao
import com.vlad.finboard.core.data.db.FinancesDao
import com.vlad.finboard.core.data.db.models.CategoryEntity
import com.vlad.finboard.core.data.db.models.FinanceEntity
import javax.inject.Inject

class FinancesRepository @Inject constructor(
    private val financesDao: FinancesDao,
    private val categoriesDao: CategoriesDao
) {

    suspend fun saveFinance(finance: FinanceEntity) {
        financesDao.insertFinance(finance)
    }

    suspend fun fetchFinances(): List<FinanceEntity> {
        return financesDao.fetchFinances()
    }

    suspend fun fetchCategory(id: Int): CategoryEntity {
        return categoriesDao.fetchCategoryById(id)
    }
}