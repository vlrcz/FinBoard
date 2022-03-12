package com.vlad.finboard.feature.finances.categories

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Singleton
class CategoriesAppInitializer @Inject constructor(
    private val categoriesManager: CategoriesManager
) {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun init() {
        scope.launch {
            categoriesManager.fetchCategories()
        }
    }
}