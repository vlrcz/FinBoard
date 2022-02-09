package com.vlad.finboard.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vlad.finboard.data.db.models.NoteEntity

@Database(
    entities = [
        NoteEntity::class
    ], version = NotesDatabase.DB_VERSION
)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun notesDao() : NotesDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "notes-database"
    }
}