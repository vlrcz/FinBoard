package com.vlad.finboard.core.tab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlad.finboard.feature.finances.list.FinancesMapper
import com.vlad.finboard.feature.finances.list.FinancesRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class TabViewModel @Inject constructor(
    private val financesRepository: FinancesRepository,
    private val financesMapper: FinancesMapper
) : ViewModel() {

    private var pieChartMap: Map<Int, Float> = emptyMap()

    private val pieChartMutableFlow = MutableStateFlow(pieChartMap)
    val pieChartFlow = pieChartMutableFlow.asStateFlow()

    fun getPieChartMapByType(type: String) {
        viewModelScope.launch {
            flow { emit(financesRepository.fetchAllFinancesWithCategoryByType(type)) }
                .catch { Timber.d("fetch all notes error ${it.localizedMessage}") }
                .flowOn(Dispatchers.IO)
                .map { financesMapper.mapEntitiesToPieChartMap(it) }
                .catch { Timber.d("map entity to pie chart map error ${it.localizedMessage}") }
                .flowOn(Dispatchers.Default)
                .collect {
                    pieChartMap = it
                    pieChartMutableFlow.value = it
                }
        }
    }
}