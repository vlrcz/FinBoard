package com.vlad.finboard.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vlad.finboard.core.data.db.models.CategoryEntity
import com.vlad.finboard.core.data.db.models.FinanceEntity
import com.vlad.finboard.core.data.db.models.FinanceWithCategoryEntity

@Dao
interface FinancesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinance(finance: FinanceEntity)

    @Query(
        "SELECT * FROM finances " +
                "INNER JOIN categories ON categories.id = finances.categoryId " +
                "WHERE categories.type = :type ORDER BY finances.createAt DESC"
    )
    suspend fun fetchFinancesWithCategoryByType(type: String): List<FinanceWithCategoryEntity>
}