package com.vlad.finboard.navigation

import androidx.fragment.app.Fragment

interface Navigator {

    fun goBack()
    fun goToMain()
}

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}