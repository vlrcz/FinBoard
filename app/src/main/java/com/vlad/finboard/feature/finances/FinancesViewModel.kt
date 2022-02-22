package com.vlad.finboard.feature.finances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class FinancesViewModel @Inject constructor(
    private val financesRepository: FinancesRepository,
    private val financesMapper: FinancesMapper
) : ViewModel() {

    private val notesMutableStateFlow = MutableStateFlow<List<NoteModel>>(emptyList())
    val notes: StateFlow<List<NoteModel>>
        get() = notesMutableStateFlow

    fun fetchNotes(type: String) {
        viewModelScope.launch {
            flow {
                emit(financesRepository.fetchNotes())
            }
                .catch { Timber.d("fetch notes error ${it.localizedMessage}") }
                .map {
                    it.map { note ->
                        val category = financesRepository.fetchCategory(note.categoryId)
                        financesMapper.mapNoteEntityToModel(note, category)
                    }
                }
                .catch { Timber.d("map entity to model error ${it.localizedMessage}") }
                .map {
                    it.filter { model ->
                        model.categoryType == type
                    }
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    notesMutableStateFlow.value = it
                }
        }
    }
}