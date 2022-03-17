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
        type = COSTS.name
    )

    private val pagingStateFlow = MutableStateFlow(state)
    val pagingState = pagingStateFlow.asStateFlow()

    private fun fetchFinances() {
        viewModelScope.launch {
            flow {
                emit(
                    financesRepository.fetchFinancesWithCategoryByType(
                        state.type,
                        PagingState.LIMIT_PER_PAGE,
                        state.offset
                    )
                )
            }
                .catch { Timber.d("fetch notes error ${it.localizedMessage}") }
                .flowOn(Dispatchers.IO)
                .map { financesMapper.mapEntities(it) }
                .catch { Timber.d("map entity to model error ${it.localizedMessage}") }
                .flowOn(Dispatchers.Default)
                .collect {
                    state = state.copy(
                            hasMore = it.size >= PagingState.LIMIT_PER_PAGE,
                            pageCount = state.pageCount + 1,
                            itemsList = (state.itemsList + it).distinct(),
                            loadingPage = false
                        )
                    pagingStateFlow.value = state
                }
        }
    }

    fun firstLoad(type: String) {
        if (!state.isFirstLoad) {
            state = state.copy(type = type, isFirstLoad = true, loadingPage = true)
            load()
        }
    }

    fun loadMore() {
        if (!state.loadingPage && state.hasMore) {
            load()
        }
    }

    fun refresh() {
        state = state.copy(pageCount = 1, hasMore = true, itemsList = emptyList(), loadingPage = true)
        load()
    }

    private fun load() {
        pagingStateFlow.value = state
        fetchFinances()
    }
}