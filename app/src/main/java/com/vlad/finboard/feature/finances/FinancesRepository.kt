package com.vlad.finboard.feature.finances

import com.vlad.finboard.core.data.db.NotesDao
import com.vlad.finboard.core.data.db.models.NoteEntity
import javax.inject.Inject

class FinancesRepository @Inject constructor(
    private val notesDao: NotesDao
) {

    suspend fun saveNote(note: NoteEntity) {
        notesDao.insertNote(note)
    }
}