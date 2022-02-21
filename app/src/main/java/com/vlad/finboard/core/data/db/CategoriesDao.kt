package com.vlad.finboard.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vlad.finboard.core.data.db.models.CategoryEntity

@Dao
interface CategoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Query("SELECT * FROM ${CategoryEntity.TABLE_NAME}")
    suspend fun fetchCategories(): List<CategoryEntity>
}