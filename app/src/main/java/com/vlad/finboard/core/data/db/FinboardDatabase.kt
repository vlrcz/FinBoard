package com.vlad.finboard.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vlad.finboard.core.data.db.models.CategoryEntity
import com.vlad.finboard.core.data.db.models.FinanceEntity

@Database(
    entities = [
        FinanceEntity::class,
        CategoryEntity::class
    ], version = FinboardDatabase.DB_VERSION
)
abstract class FinboardDatabase : RoomDatabase() {

    abstract fun notesDao(): FinancesDao
    abstract fun categoriesDao(): CategoriesDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "finboard-database"
    }
}