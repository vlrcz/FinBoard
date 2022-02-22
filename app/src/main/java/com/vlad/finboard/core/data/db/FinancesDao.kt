package com.vlad.finboard.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vlad.finboard.core.data.db.models.FinanceEntity

@Dao
interface FinancesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinance(finance: FinanceEntity)

    @Query("SELECT * FROM ${FinanceEntity.TABLE_NAME}")
    suspend fun fetchFinances(): List<FinanceEntity>
}