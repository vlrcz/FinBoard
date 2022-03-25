package com.vlad.finboard.feature.categories

import com.vlad.finboard.feature.finances.model.CategoryModel
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber

@Singleton
class CategoriesManager @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
    private val categoriesMapper: CategoriesMapper
) {

    private val categoriesStateFlow =
        MutableStateFlow<Map<String, List<CategoryModel>>>(emptyMap())
    val categoriesFlow = categoriesStateFlow.asStateFlow()

    suspend fun fetchCategories() {
        flow { emit(categoriesRepository.fetchCategoriesList()) }
            .catch { Timber.e("fetch categories from db error ${it.localizedMessage}") }
            .flowOn(Dispatchers.IO)
            .map { it.map { categoriesMapper.mapEntityToModel(it) }.groupBy { it.type } }
            .catch { Timber.e("map categories error ${it.localizedMessage}") }
            .flowOn(Dispatchers.Default)
            .collect { categoriesStateFlow.value = it }
    }
}