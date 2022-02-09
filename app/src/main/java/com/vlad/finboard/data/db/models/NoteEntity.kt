package com.vlad.finboard.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = NoteEntity.TABLE_NAME
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val type: NotesType,
    val category: NotesCategories,
    val sum: Int
) {
    companion object {
        const val TABLE_NAME = "notes"
    }
}
