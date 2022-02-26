package com.vlad.finboard.feature.finances.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlad.finboard.feature.finances.model.FinanceWithDate
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

    private val financesMutableStateFlow = MutableStateFlow<List<FinanceWithDate>>(emptyList())
    val finances: StateFlow<List<FinanceWithDate>>
        get() = financesMutableStateFlow

    fun fetchFinances(type: String) {
        viewModelScope.launch {
            flow {
                emit(financesRepository.fetchFinances(type))
            }
                .catch { Timber.d("fetch notes error ${it.localizedMessage}") }
                .map { listEntity ->
                    listEntity.map { financesMapper.mapFinanceEntityToModel(it.key, it.value) }
                }
                .catch { Timber.d("map entity to model error ${it.localizedMessage}") }
                .flowOn(Dispatchers.IO)
                .map { listModel ->
                    listModel
                        .groupBy { it.createAt }
                        .flatMap {
                            listOf(it.key) + it.value
                        }
                }
                .flowOn(Dispatchers.Default)
                .collect {
                    financesMutableStateFlow.value = it
                }
        }
    }
}