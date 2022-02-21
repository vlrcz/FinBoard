package com.vlad.finboard.core.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = CategoryEntity.TABLE_NAME
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val type: String,
    val color: String,
    val res: String
) {
    companion object {
        const val TABLE_NAME = "categories"
    }
}
