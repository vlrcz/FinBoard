package com.vlad.finboard.feature.finances.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlad.finboard.core.data.db.models.FinanceEntity
import com.vlad.finboard.feature.finances.FinancesRepository
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class FinancesDetailViewModel @Inject constructor(
    private val financesRepository: FinancesRepository
) : ViewModel() {

    fun saveFinance(categoryId: Int, sum: String, date: String) {
        viewModelScope.launch {
            val uniqueId = UUID.randomUUID().toString()
            flow {
                emit(FinanceEntity(uniqueId, categoryId, sum, date))
            }
                .onEach {
                    financesRepository.saveFinance(it)
                }
                .catch {
                    Timber.d ("save note error ${it.localizedMessage}")
                }
                .flowOn(Dispatchers.IO)
                .collect()
        }
    }
}