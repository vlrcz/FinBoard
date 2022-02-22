package com.vlad.finboard.feature.finances

import android.content.Context
import android.graphics.Color
import com.vlad.finboard.core.data.db.models.CategoryEntity
import com.vlad.finboard.core.data.db.models.NoteEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FinancesMapper @Inject constructor(
    private val context: Context
) {

    fun mapNoteEntityToModel(entity: NoteEntity, category: CategoryEntity): NoteModel {
        val drawable =
            context.resources.getIdentifier(category.res, "drawable", context.packageName)
        val color = Color.parseColor(category.color)
        return NoteModel(
            id = entity.id,
            categoryName = category.name,
            categoryColor = color,
            categoryDrawable = drawable,
            categoryType = category.type,
            sum = entity.sum,
            date = entity.date
        )
    }
}
