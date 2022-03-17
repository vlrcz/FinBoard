package com.vlad.finboard.feature.finances.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlad.finboard.core.data.db.models.FinanceEntity
import com.vlad.finboard.feature.categories.CategoriesManager
import com.vlad.finboard.feature.finances.list.FinancesRepository
import com.vlad.finboard.feature.finances.model.FinanceModel
import com.vlad.finboard.feature.finances.types.FinancesType.COSTS
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class FinancesDetailViewModel @Inject constructor(
    private val financesRepository: FinancesRepository,
    private val categoriesManager: CategoriesManager
) : ViewModel() {

    private var state = DetailState(
        categoriesList = emptyList(),
        selectedCategoryId = 1,
        financeId = UUID.randomUUID().toString(),
        sum = 0.0,
        type = COSTS.name,
        createAt = System.currentTimeMillis()
    )

    private val detailStateFlow = MutableStateFlow(state)
    val detailState = detailStateFlow.asStateFlow()
    private val saveSuccessSharedFlow = MutableSharedFlow<Boolean>(1)
    val saveSuccess = saveSuccessSharedFlow.asSharedFlow()

    fun clickedOnCategory(id: Int) {
        val list = state.categoriesList
        list.forEach { it.isSelected = it.id == id }
        state = state.copy(selectedCategoryId = id, categoriesList = list)
        detailStateFlow.tryEmit(state)
    }

    fun restoreStateFromArgs(model: FinanceModel) {
        state = state.copy(
            selectedCategoryId = model.categoryId,
            financeId = model.id,
            sum = model.sum.sumDouble,
            createAt = model.createAt.dateMillis
        )
        fetchCategoriesByType(model.type)
        detailStateFlow.tryEmit(state)
    }

    fun fetchCategoriesByType(type: String) {
        state = state.copy(type = type)
        viewModelScope.launch {
            categoriesManager.categories
                .map { it.filter { it.type == state.type } }
                .onEach {
                    it.forEach {model ->
                        model.isSelected = model.id == state.selectedCategoryId
                    }
                }
                .collect {
                    state = state.copy(categoriesList = it)
                    detailStateFlow.tryEmit(state)
                }
        }
    }

    fun saveFinance(sum: Double) {
        viewModelScope.launch {
            flow {
                emit(
                    FinanceEntity(
                        id = state.financeId,
                        categoryId = state.selectedCategoryId,
                        sum = sum,
                        type = state.type,
                        createAt = state.createAt,
                        updateAt = System.currentTimeMillis()
                    )
                )
            }
                .onEach { financesRepository.saveFinance(it) }
                .catch { Timber.d("save finance error ${it.localizedMessage}") }
                .flowOn(Dispatchers.IO)
                .collect { saveSuccessSharedFlow.tryEmit(true) }
        }
    }
}