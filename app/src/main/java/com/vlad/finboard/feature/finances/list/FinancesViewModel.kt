package com.vlad.finboard.feature.finances.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlad.finboard.feature.finances.FinancesConstants.LIMIT_PER_PAGE
import com.vlad.finboard.feature.finances.model.DateModel
import com.vlad.finboard.feature.finances.model.FinanceModel
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class FinancesViewModel @Inject constructor(
    private val financesRepository: FinancesRepository,
    private val financesMapper: FinancesMapper
) : ViewModel() {

    private val stateChannel = Channel<FinancesListState>(Channel.BUFFERED)
    val state = stateChannel.receiveAsFlow()

    private var pagingState = PagingState(
        limit = LIMIT_PER_PAGE,
        loadingPage = false,
        pageCount = 1,
        itemsList = emptyList(),
        hasMore = true,
        isFirstLoad = false
    )

    private fun fetchFinances(type: String) {
        viewModelScope.launch {
            flow { emit(financesRepository.fetchFinancesWithCategoryByType(type, pagingState.limit, pagingState.offset)) }
                .catch { Timber.d("fetch notes error ${it.localizedMessage}") }
                .flowOn(Dispatchers.IO)
                .map {
                    pagingState.hasMore = it.size >= LIMIT_PER_PAGE
                    financesMapper.mapEntities(it)
                }
                .catch { Timber.d("map entity to model error ${it.localizedMessage}") }
                .flowOn(Dispatchers.Default)
                .collect {
                    with(pagingState) {
                        pageCount++
                        pagingState.loadingPage = false
                    }
                    pagingState = pagingState.copy(itemsList = (pagingState.itemsList + it).distinct())
                    stateChannel.send(FinancesListState.Loading(false))
                    stateChannel.send(FinancesListState.FinancesList(pagingState.itemsList))
                }
        }
    }

    fun firstLoad(type: String) {
        if (!pagingState.isFirstLoad) {
            load(type)
            pagingState.isFirstLoad = true
        }
    }

    fun loadMore(type: String) {
        if (!pagingState.loadingPage && pagingState.hasMore) {
            load(type)
        }
    }

    fun refresh(type: String) {
        pagingState = pagingState.copy(pageCount = 1, hasMore = true, itemsList = emptyList())
        load(type)
    }

    private fun load(type: String) {
        viewModelScope.launch {
            pagingState.loadingPage = true
            stateChannel.send(FinancesListState.Loading(true))
            fetchFinances(type)
        }
    }


    fun openFinancesDetailButtonClicked() {
        viewModelScope.launch {
            stateChannel.send(FinancesListState.NavigateToFinancesDetail)
        }
    }

    fun onFinancesItemClicked(model: FinanceModel) {
        viewModelScope.launch {
            stateChannel.send(FinancesListState.EditFinancesDetail(model))
        }
    }
}