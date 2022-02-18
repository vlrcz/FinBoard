package com.vlad.finboard.data.db.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = NoteEntity.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"]
        )
    ]
)
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val categoryId: Int,
    val sum: String,
    val date: String
) {
    companion object {
        const val TABLE_NAME = "notes"
    }
}
