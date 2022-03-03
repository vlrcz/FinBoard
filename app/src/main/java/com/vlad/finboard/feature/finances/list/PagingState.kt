package com.vlad.finboard.feature.finances.list

import com.vlad.finboard.feature.finances.model.Item

data class PagingState(
    val limit: Int,
    var loadingPage: Boolean,
    var pageCount: Int,
    val itemsList: List<Item>,
    var hasMore: Boolean,
    var isFirstLoad: Boolean
) {
    val offset
        get() = (pageCount - 1) * limit
}