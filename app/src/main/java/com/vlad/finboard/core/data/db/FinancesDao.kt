package com.vlad.finboard.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vlad.finboard.core.data.db.models.CategoryEntity
import com.vlad.finboard.core.data.db.models.FinanceEntity
import com.vlad.finboard.core.data.db.models.FinanceWithCategoryEntity

@Dao
interface FinancesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinance(finance: FinanceEntity)

    @Transaction
    @Query(
        "SELECT * FROM finances " +
                "INNER JOIN categories ON finances.categoryId = categories.id " +
                "WHERE categories.type = :type ORDER BY finances.createAt DESC"
    )
    suspend fun fetchFinancesWithCategoryByType(type: String): List<FinanceWithCategoryEntity>
}