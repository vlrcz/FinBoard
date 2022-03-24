package com.vlad.finboard.feature.finances.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlad.finboard.core.data.db.models.FinanceEntity
import com.vlad.finboard.feature.categories.CategoriesManager
import com.vlad.finboard.feature.finances.detail.FinancesDetailState.Companion.DEFAULT_SUM
import com.vlad.finboard.feature.finances.list.FinancesRepository
import com.vlad.finboard.feature.finances.model.CategoryModel
import com.vlad.finboard.feature.finances.model.FinanceModel
import com.vlad.finboard.feature.finances.types.FinancesType.COSTS
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
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

    private var state = FinancesDetailState(
        financeId = UUID.randomUUID().toString(),
        createAt = System.currentTimeMillis(),
        categoriesList = emptyList(),
        selectedCategoryId = 1,
        type = COSTS.name,
        isSaveSuccess = false, //todo В sharedFlow
        sum = DEFAULT_SUM
    )

    private val detailStateFlow = MutableStateFlow(state)
    val detailFlow = detailStateFlow.asStateFlow()

    fun fetchCategoriesByType(type: String) {
        viewModelScope.launch {
            categoriesManager.categoriesFlow
                .map { it[type] }
                .filterNotNull()
                .onEach {  }
                .flowOn(Dispatchers.Default)
                .collect {
                    state =
                        state.copy(categoriesList = it, selectedCategoryId = it[0].id, type = type)
                    detailStateFlow.value = state
                }
        }
    }

    //todo переименовать
    fun restoreStateFromFinanceModel(model: FinanceModel) {
        fetchCategoriesByType(model.type)
        val list = mapListBySelectedCategory(model.categoryId)
        state = state.copy(
            financeId = model.id,
            createAt = model.createAt.dateMillis,
            sum = model.sum.sumDouble,
            selectedCategoryId = model.categoryId,
            categoriesList = list
        )
        detailStateFlow.value = state
    }

    fun clickedOnCategory(id: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val list = mapListBySelectedCategory(id)
            state = state.copy(selectedCategoryId = id, categoriesList = list)
            detailStateFlow.value = state
        }
    }

    private fun mapListBySelectedCategory(id: Int): List<CategoryModel> {
        return state.categoriesList.map {
            it.copy(isSelected = it.id == id)
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
                .collect {
                    state = state.copy(isSaveSuccess = true)
                    detailStateFlow.value = state
                }
        }
    }
}