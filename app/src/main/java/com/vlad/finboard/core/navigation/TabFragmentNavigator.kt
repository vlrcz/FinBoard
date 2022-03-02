package com.vlad.finboard.core.navigation

import androidx.fragment.app.Fragment
import com.vlad.finboard.R
import com.vlad.finboard.core.navigation.screen.NavigationScreen
import com.vlad.finboard.core.navigation.screen.TabScreen
import com.vlad.finboard.core.tab.TabConfig

class TabFragmentNavigator(
    fragment: Fragment,
    tabConfig: TabConfig,
    private val tabSelectedListener: (Int) -> Unit
) : Navigator(fragment.requireActivity(), R.id.tabContainer, fragment.childFragmentManager) {

    private var currentPosition: Int? = null
    private val config = tabConfig.config

    override fun navigate(screen: NavigationScreen): Boolean {
        return when (screen) {
            is TabScreen -> {
                val tabPosition = config[screen.redirect.tag]
                    ?: throw IllegalArgumentException()

                if (currentPosition != tabPosition) {
                    tabSelectedListener.invoke(tabPosition)
                    currentPosition = tabPosition
                }
                super.navigate(screen.redirect)
            }
            else -> false
        }
    }

    fun restoreState() {
        currentPosition?.let { tabSelectedListener.invoke(it) }
    }
}