package com.vlad.finboard.core.data.db.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = FinanceEntity.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"]
        )
    ]
)
data class FinanceEntity(
    @PrimaryKey
    val id: String,
    val categoryId: Int,
    val sum: Double,
    val createAt: Long,
    val updateAt: Long
) {
    companion object {
        const val TABLE_NAME = "notes"
    }
}
