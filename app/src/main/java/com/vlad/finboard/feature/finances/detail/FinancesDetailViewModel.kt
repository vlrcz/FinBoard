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

    fun saveFinance(
        financeId: String?,
        categoryId: Int,
        sum: Double,
        createAt: Long,
        updateAt: Long
    ) {
        viewModelScope.launch {
            val uniqueId = financeId ?: UUID.randomUUID().toString()
            flow {
                emit(FinanceEntity(uniqueId, categoryId, sum, createAt, updateAt))
            }
                .onEach {
                    financesRepository.saveFinance(it)
                }
                .catch {
                    Timber.d("save finance error ${it.localizedMessage}")
                }
                .flowOn(Dispatchers.IO)
                .collect()
        }
    }
}