package com.vlad.finboard.navigation

import androidx.fragment.app.Fragment
import com.vlad.finboard.R
import com.vlad.finboard.navigation.screen.Screen
import com.vlad.finboard.navigation.screen.TabScreen

class TabFragmentNavigator(fragment: Fragment) :
    Navigator(fragment.requireActivity(), R.id.tabContainer, fragment.childFragmentManager) {

    override fun navigate(screen: Screen): Boolean {
        return when (screen) {
            is TabScreen -> {
                super.navigate(screen.redirect)
            }
            else -> {
                false
            }
        }
    }
}