package com.vlad.finboard.core.navigation

import androidx.fragment.app.Fragment
import com.vlad.finboard.R
import com.vlad.finboard.core.navigation.screen.NavigationScreen
import com.vlad.finboard.core.navigation.screen.TabScreen

class TabFragmentNavigator(fragment: Fragment) :
    Navigator(fragment.requireActivity(), R.id.tabContainer, fragment.childFragmentManager) {

    override fun navigate(screen: NavigationScreen): Boolean {
        return when (screen) {
            is TabScreen -> super.navigate(screen.redirect)
            else -> false
        }
    }
}