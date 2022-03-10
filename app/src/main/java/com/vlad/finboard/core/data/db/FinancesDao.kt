package com.vlad.finboard.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vlad.finboard.core.data.db.models.FinanceEntity
import com.vlad.finboard.core.data.db.models.FinanceWithCategoryEntity

@Dao
interface FinancesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinance(finance: FinanceEntity)

    @Transaction
    @Query(
        "SELECT * FROM finances WHERE finances.type = :type " +
                "ORDER BY finances.createAt DESC LIMIT :limit OFFSET :offset"
    )
    suspend fun fetchFinancesWithCategoryByType(type: String, limit: Int, offset: Int): List<FinanceWithCategoryEntity>
}