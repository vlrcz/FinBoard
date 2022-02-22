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

    private val notesMutableStateFlow = MutableStateFlow<List<FinanceModel>>(emptyList())
    val notes: StateFlow<List<FinanceModel>>
        get() = notesMutableStateFlow

    fun fetchNotes(type: String) {
        viewModelScope.launch {
            flow {
                emit(financesRepository.fetchFinances())
            }
                .catch { Timber.d("fetch notes error ${it.localizedMessage}") }
                .map { listEntity ->
                    listEntity
                        .map { finance ->
                            val category = financesRepository.fetchCategory(finance.categoryId)
                            financesMapper.mapFinanceEntityToModel(finance, category)
                        }
                        .sortedByDescending {
                            it.date
                        }
                }
                .catch { Timber.d("map entity to model error ${it.localizedMessage}") }
                .map { listModel ->
                    var date = ""
                    val mappedList = mutableListOf<FinanceModel>()
                    listModel.forEach {
                        if (it.date != date) {
                            mappedList.addAll(listOf(it.copy(isDate = true), it))
                            date = it.date
                        } else {
                            mappedList.add(it)
                        }
                    }
                    mappedList.filter { it.categoryType == type }
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    notesMutableStateFlow.value = it
                }
        }
    }
}