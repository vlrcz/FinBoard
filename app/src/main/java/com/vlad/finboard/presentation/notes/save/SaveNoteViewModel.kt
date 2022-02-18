package com.vlad.finboard.presentation.notes.save

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlad.finboard.data.db.models.CategoryMapper
import com.vlad.finboard.data.db.models.CategoryModel
import com.vlad.finboard.data.db.models.NoteEntity
import com.vlad.finboard.data.db.models.NotesType
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SaveNoteViewModel @Inject constructor(
    private val saveNoteRepository: SaveNoteRepository,
    private val categoryMapper: CategoryMapper
) : ViewModel() {

    companion object {
        const val INCOME = "income"
        const val COSTS = "costs"
    }

    private val categoriesStateFlow = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories: StateFlow<List<CategoryModel>>
        get() = categoriesStateFlow

    fun fetchCategories(flag: String) {
        viewModelScope.launch {
            flow {
                emit(saveNoteRepository.fetchCategoriesList())
            }
                .catch { "fetch categories from db error ${it.localizedMessage}" }
                .map {
                    it.map {
                        categoryMapper.mapEntityToModel(it)
                    }
                }
                .map {
                    when (flag) {
                        COSTS -> {
                            it.filter {
                                it.type == NotesType.COSTS.toString()
                            }
                        }
                        INCOME -> {
                            it.filter {
                                it.type == NotesType.INCOME.toString()
                            }
                        }
                        else -> {
                            emptyList()}
                    }
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    categoriesStateFlow.value = it
                }
        }
    }

    fun saveNote(categoryId: Int, sum: String, date: String) {
        viewModelScope.launch {
            val uniqueId = UUID.randomUUID().toString()
            flow {
                emit(NoteEntity(uniqueId, categoryId, sum, date))
            }
                .onEach {
                    saveNoteRepository.saveNote(it)
                }
                .flowOn(Dispatchers.IO)
                .collect()
        }
    }
}