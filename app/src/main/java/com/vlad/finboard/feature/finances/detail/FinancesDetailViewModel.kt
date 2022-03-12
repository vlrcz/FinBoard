package com.vlad.finboard.feature.finances.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlad.finboard.core.data.db.models.FinanceEntity
import com.vlad.finboard.feature.finances.categories.CategoriesManager
import com.vlad.finboard.feature.finances.categories.CategoriesMapper
import com.vlad.finboard.feature.finances.categories.CategoriesRepository
import com.vlad.finboard.feature.finances.list.FinancesRepository
import com.vlad.finboard.feature.finances.model.CategoryModel
import com.vlad.finboard.feature.finances.model.FinanceModel
import com.vlad.finboard.feature.finances.types.FinancesType.COSTS
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
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

    data class DetailState(
        val categoriesList: List<CategoryModel>,
        val selectedCategoryId: Int,
        val financeId: String,
        val sum: Double,
        val type: String,
        val createAt: Long,
        val updateAt: Long
    )

    private var state = DetailState(
        categoriesList = emptyList(),
        selectedCategoryId = 1,
        financeId = UUID.randomUUID().toString(),
        sum = 0.0,
        type = COSTS.name,
        createAt = System.currentTimeMillis(),
        updateAt = System.currentTimeMillis()
    )

    private val detailStateFlow = MutableStateFlow(state)
    val detailState: StateFlow<DetailState> = detailStateFlow
    private val saveSuccessSharedFlow = MutableSharedFlow<Boolean>(1)
    val saveSuccess: SharedFlow<Boolean> = saveSuccessSharedFlow

    fun changeCategory(id: Int) {
        val list = state.categoriesList
        list.forEach { it.isSelected = it.id == id }
        state = state.copy(selectedCategoryId = id, categoriesList = list)
        detailStateFlow.value = state
    }

    fun restoreStateFromArgs(model: FinanceModel) {
        state = state.copy(
            selectedCategoryId = model.categoryId,
            financeId = model.id,
            sum = model.sum.sumDouble,
            type = model.type,
            createAt = model.createAt.dateMillis
        )
        detailStateFlow.value = state
    }

    fun fetchCategories(type: String) {
        viewModelScope.launch {
            categoriesManager.categories
                .map {
                    it.filter { it.type == type } }
                .onEach {
                    it.forEach {model ->
                        model.isSelected = model.id == state.selectedCategoryId
                    }
                }
                .collect {
                    state = state.copy(categoriesList = it, type = type)
                    detailStateFlow.value = state
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
                        updateAt = state.updateAt
                    )
                )
            }
                .onEach { financesRepository.saveFinance(it) }
                .catch { Timber.d("save finance error ${it.localizedMessage}") }
                .flowOn(Dispatchers.IO)
                .collect { saveSuccessSharedFlow.tryEmit(true) }
        }
    }

    /*fun fetchCategories(type: String) {
        flow { emit(categoriesRepository.fetchCategoriesListByType(type)) }
            .catch { Timber.e("fetch categories from db error ${it.localizedMessage}") }
            .flowOn(Dispatchers.IO)
            .map { it.map { categoriesMapper.mapEntityToModel(it) } }
            .catch { Timber.e("map categories error ${it.localizedMessage}") }
            .flowOn(Dispatchers.Default)
            .onEach {
                it.forEach {model ->
                    model.isSelected = model.id == state.selectedCategoryId
                }
            }
            .collect {
                state = state.copy(categoriesList = it, type = type)
                detailStateFlow.value = state
            }
    }*/
}