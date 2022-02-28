package com.vlad.finboard.core.data.db.models

import androidx.room.Embedded
import androidx.room.Relation

data class FinanceWithCategoryEntity(
    @Relation(parentColumn = "id", entityColumn = "categoryId", entity = FinanceEntity::class)
    val financeEntity: FinanceEntity,
    @Embedded
    val categoryEntity: CategoryEntity
)
