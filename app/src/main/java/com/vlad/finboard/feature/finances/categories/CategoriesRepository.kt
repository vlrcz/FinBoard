package com.vlad.finboard.feature.finances.categories

import com.vlad.finboard.core.data.db.CategoriesDao
import com.vlad.finboard.core.data.db.models.CategoryEntity
import javax.inject.Inject

class CategoriesRepository @Inject constructor(
    private val categoriesDao: CategoriesDao
) {
    suspend fun fetchCategory(id: Int): CategoryEntity {
        return categoriesDao.fetchCategoryById(id)
    }

    suspend fun fetchCategoriesList(): List<CategoryEntity> {
        return categoriesDao.fetchCategories()
    }
}