package com.vlad.finboard.core.data.db.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "finances",
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
    val type: String,
    val createAt: Long,
    val updateAt: Long
)