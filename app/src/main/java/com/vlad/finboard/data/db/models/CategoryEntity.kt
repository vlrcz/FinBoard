package com.vlad.finboard.data.db.models

import androidx.annotation.ColorRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = CategoryEntity.TABLE_NAME
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val type: String,
    @ColorRes
    val color: Int
) {
    companion object {
        const val TABLE_NAME = "categories"
    }
}

/*
CAFE,
TOURISM,
FOOD,
TECHNICS,
HEALTH,
CLOTHES,
GAMES,
SPORT*/
