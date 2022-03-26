package com.vlad.finboard.feature.finances.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlad.finboard.core.data.db.models.FinanceEntity
import com.vlad.finboard.feature.categories.CategoriesManager
import com.vlad.finboard.feature.finances.list.FinancesRepository
import com.vlad.finboard.feature.finances.model.FinanceModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
        categoriesList = emptyList(),
        selectedCategoryId = -1
    )

    private val categoriesStateFlow = MutableStateFlow(state)
    val categoriesFlow = categoriesStateFlow.asStateFlow()
    private val saveSuccessSharedFlow = MutableSharedFlow<Boolean>()
    val saveSuccessFlow = saveSuccessSharedFlow.asSharedFlow()

    fun fetchCategoriesByType(type: String) {
        viewModelScope.launch {
            categoriesManager.categoriesFlow
                .map { it[type] }
                .filterNotNull()
                .map {
                    it.mapIndexed { index, model ->
                        val isSelected =
                            if (state.selectedCategoryId != -1) state.selectedCategoryId == model.id else index == 0
                        model.copy(isSelected = isSelected)
                    }
                }
                .flowOn(Dispatchers.Default)
                .collect {
                    val selectedCategoryId =
                        if (state.selectedCategoryId == -1) it[0].id else state.selectedCategoryId
                    state = state.copy(
                        categoriesList = it,
                        selectedCategoryId = selectedCategoryId
                    )
                    categoriesStateFlow.value = state
                }
        }
    }

    fun fillStateFromFinanceModel(model: FinanceModel) {
        state = state.copy(selectedCategoryId = model.categoryId)
        fetchCategoriesByType(model.type)
    }

    fun clickedOnCategory(id: Int) {
        state = state.copy(
            selectedCategoryId = id,
            categoriesList = state.categoriesList.map { it.copy(isSelected = it.id == id) })
        categoriesStateFlow.value = state
    }

    fun saveFinance(type: String, sum: String) {
        viewModelScope.launch {
            flow {
                emit(
                    FinanceEntity(
                        id = UUID.randomUUID().toString(),
                        categoryId = state.selectedCategoryId,
                        sum = sum.toBigDecimal(),
                        type = type,
                        createAt = System.currentTimeMillis()
                    )
                )
            }
                .onEach { financesRepository.saveFinance(it) }
                .catch { Timber.d("save finance error ${it.localizedMessage}") }
                .flowOn(Dispatchers.IO)
                .collect { saveSuccessSharedFlow.emit(true) }
        }
    }

    fun updateFinance(model: FinanceModel, sum: String) {
        viewModelScope.launch {
            flow {
                emit(
                    FinanceEntity(
                        id = model.id,
                        categoryId = state.selectedCategoryId,
                        sum = sum.toBigDecimal(),
                        type = model.type,
                        createAt = model.createAt.dateMillis
                    )
                )
            }
                .onEach { financesRepository.updateFinance(it) }
                .catch { Timber.d("update finance error ${it.localizedMessage}") }
                .flowOn(Dispatchers.IO)
                .collect { saveSuccessSharedFlow.emit(true) }
        }
    }
}