package com.vlad.finboard.presentation.notes.save

import com.vlad.finboard.data.db.CategoriesDao
import com.vlad.finboard.data.db.NotesDao
import com.vlad.finboard.data.db.models.CategoryEntity
import com.vlad.finboard.data.db.models.NoteEntity
import javax.inject.Inject

class SaveNoteRepository @Inject constructor(
    private val notesDao: NotesDao,
    private val categoriesDao: CategoriesDao
) {

    suspend fun fetchCategoriesList(): List<CategoryEntity> {
        return categoriesDao.fetchCategories()
    }

    suspend fun saveNote(note: NoteEntity) {
        notesDao.insertNote(note)
    }

}