package com.vlad.finboard.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vlad.finboard.core.data.db.models.NoteEntity

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Query("SELECT * FROM ${NoteEntity.TABLE_NAME}")
    suspend fun fetchNotes(): List<NoteEntity>
}