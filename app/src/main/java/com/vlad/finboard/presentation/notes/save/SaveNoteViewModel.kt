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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class SaveNoteViewModel @Inject constructor(
    private val saveNoteRepository: SaveNoteRepository,
    private val categoryMapper: CategoryMapper
) : ViewModel() {

    companion object {
        const val INCOME = "income"
        const val COSTS = "costs"
    }

    private val categoriesStateFlow = MutableSharedFlow<List<CategoryModel>>(1)
    val categories: SharedFlow<List<CategoryModel>>
        get() = categoriesStateFlow

    fun isSelected(model: CategoryModel) {
        viewModelScope.launch {
            val list = categoriesStateFlow.first()
            list.forEach {
                if (it.id == model.id) {
                    it.isSelected = !model.isSelected
                } else {
                    it.isSelected = false
                }
            }
            categoriesStateFlow.tryEmit(list)
        }
    }

    fun fetchCategories(flag: String) {
        viewModelScope.launch {
            flow {
                emit(saveNoteRepository.fetchCategoriesList())
            }
                .catch { Timber.e("fetch categories from db error ${it.localizedMessage}") }
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
                            emptyList()
                        }
                    }
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    categoriesStateFlow.tryEmit(it)
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
                .catch { Timber.e("save note error ${it.localizedMessage}") }
                .flowOn(Dispatchers.IO)
                .collect()
        }
    }
}