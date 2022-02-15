package com.vlad.finboard.data.db.models

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
    val color: String
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
SPORT,
RENTAL,

SALARY,
DEPOSITS,
GRANTS,
PENSION,
RENTAL*/
