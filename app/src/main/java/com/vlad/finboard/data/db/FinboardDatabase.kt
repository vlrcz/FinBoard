package com.vlad.finboard.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vlad.finboard.data.db.models.CategoryEntity
import com.vlad.finboard.data.db.models.NoteEntity

@Database(
    entities = [
        NoteEntity::class,
        CategoryEntity::class
    ], version = FinboardDatabase.DB_VERSION
)
abstract class FinboardDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDao
    abstract fun categoriesDao(): CategoriesDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "finboard-database"
    }
}