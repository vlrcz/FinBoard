package com.vlad.finboard.core.data.db.models

import androidx.room.Embedded
import androidx.room.Relation

data class FinanceWithCategoryEntity(
    @Embedded
    val financeEntity: FinanceEntity,
    @Relation(parentColumn = "categoryId", entityColumn = "id")
    val categoryEntity: CategoryEntity
)
