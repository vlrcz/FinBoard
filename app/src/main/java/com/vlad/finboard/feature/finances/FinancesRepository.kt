package com.vlad.finboard.feature.finances

import com.vlad.finboard.core.data.db.CategoriesDao
import com.vlad.finboard.core.data.db.NotesDao
import com.vlad.finboard.core.data.db.models.CategoryEntity
import com.vlad.finboard.core.data.db.models.NoteEntity
import javax.inject.Inject

class FinancesRepository @Inject constructor(
    private val notesDao: NotesDao,
    private val categoriesDao: CategoriesDao
) {

    suspend fun saveNote(note: NoteEntity) {
        notesDao.insertNote(note)
    }

    suspend fun fetchNotes(): List<NoteEntity> {
        return notesDao.fetchNotes()
    }

    suspend fun fetchCategory(id: Int): CategoryEntity {
        return categoriesDao.fetchCategoryById(id)
    }
}