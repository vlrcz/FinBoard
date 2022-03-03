package com.vlad.finboard.feature.finances.list

import com.vlad.finboard.feature.finances.model.FinanceModel
import com.vlad.finboard.feature.finances.model.Item

sealed class FinancesListState {
    data class Loading(val isLoading: Boolean = false) : FinancesListState()
    data class FinancesList(val list: List<Item> = emptyList()) : FinancesListState()
    object NavigateToFinancesDetail : FinancesListState()
    data class EditFinancesDetail(val model: FinanceModel) : FinancesListState()
}