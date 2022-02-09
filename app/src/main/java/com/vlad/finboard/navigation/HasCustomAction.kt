package com.vlad.finboard.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface HasCustomAction {

    fun getCustomAction(): CustomAction
}

data class CustomAction(
    @DrawableRes
    val iconRes: Int,
    @StringRes
    val textRes: Int,
    val onCustomAction: Runnable
)