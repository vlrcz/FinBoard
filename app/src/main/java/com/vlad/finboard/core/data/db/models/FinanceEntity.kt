package com.vlad.finboard.core.data.db.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vlad.finboard.core.data.db.BigDecimalStringTypeConverter
import java.math.BigDecimal

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
@TypeConverters(BigDecimalStringTypeConverter::class)
data class FinanceEntity(
    @PrimaryKey
    val id: String,
    val categoryId: Int,
    val sum: BigDecimal,
    val type: String,
    val createAt: Long,
    val updateAt: Long = System.currentTimeMillis()
)