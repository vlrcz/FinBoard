package com.vlad.finboard.feature.finances.list

import com.vlad.finboard.feature.finances.model.Item

data class PagingState(
    val loadingPage: Boolean,
    val pageCount: Int,
    val itemsList: List<Item>,
    val hasMore: Boolean,
    val isFirstLoad: Boolean,
    val type: String,
    val pieChartMap: Map<Int, Float>
) {
    val offset
        get() = (pageCount - 1) * LIMIT_PER_PAGE

    companion object {
        const val LIMIT_PER_PAGE = 15
    }
}