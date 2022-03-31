package com.vlad.finboard.feature.finances.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlad.finboard.feature.finances.types.FinancesType.COSTS
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

class FinancesViewModel @Inject constructor(
    private val financesRepository: FinancesRepository,
    private val financesMapper: FinancesMapper
) : ViewModel() {

    private var state = PagingState(
        loadingPage = false,
        pageCount = 1,
        itemsList = emptyList(),
        hasMore = true,
        isFirstLoad = false,
        type = COSTS.name,
        pieChartMap = emptyMap()
    )

    private val pagingStateFlow = MutableStateFlow(state)
    val pagingState = pagingStateFlow.asStateFlow()

    private fun fetchLimitedFinances() {
        viewModelScope.launch {
            flow {
                emit(
                    financesRepository.fetchLimitedFinancesWithCategoryByType(
                        state.type,
                        PagingState.LIMIT_PER_PAGE,
                        state.offset
                    )
                )
            }
                .catch { Timber.d("fetch notes with limit error ${it.localizedMessage}") }
                .flowOn(Dispatchers.IO)
                .map { financesMapper.mapEntitiesToItems(it) }
                .catch { Timber.d("map entity to items error ${it.localizedMessage}") }
                .flowOn(Dispatchers.Default)
                .collect {
                    state = state.copy(
                            hasMore = it.size >= PagingState.LIMIT_PER_PAGE,
                            pageCount = state.pageCount + 1,
                            itemsList = if (state.pageCount == 1) it else state.itemsList + it,
                            loadingPage = false
                        )
                    pagingStateFlow.value = state
                }
        }
    }

    private fun fetchAllFinances() {
        viewModelScope.launch {
            flow { emit(financesRepository.fetchAllFinancesWithCategoryByType(state.type)) }
                .catch { Timber.d("fetch all notes error ${it.localizedMessage}") }
                .flowOn(Dispatchers.IO)
                .map { financesMapper.mapEntitiesToPieChartMap(it) }
                .catch { Timber.d("map entity to pie chart map error ${it.localizedMessage}") }
                .flowOn(Dispatchers.Default)
                .collect {
                    state = state.copy(pieChartMap = it)
                    pagingStateFlow.value = state
                }
        }
    }

    fun firstLoad(type: String) {
        if (!state.isFirstLoad) {
            state = state.copy(type = type, isFirstLoad = true, loadingPage = true)
            fetchAllFinances()
            load()
        }
    }

    fun loadMore() {
        if (!state.loadingPage && state.hasMore) {
            load()
        }
    }

    fun refresh() {
        state = state.copy(pageCount = 1, hasMore = true, loadingPage = true)
        fetchAllFinances()
        load()
    }

    private fun load() {
        pagingStateFlow.value = state
        fetchLimitedFinances()
    }
}