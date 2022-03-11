package com.vlad.finboard.core.navigation

import androidx.fragment.app.Fragment
import com.vlad.finboard.R
import com.vlad.finboard.core.navigation.screen.NavigationScreen
import com.vlad.finboard.core.navigation.screen.TabScreen
import com.vlad.finboard.core.tab.TabConfig

class TabFragmentNavigator(
    val fragment: Fragment,
    tabConfig: TabConfig,
    private val tabSelectedListener: (Int) -> Unit
) : Navigator(fragment.requireActivity(), R.id.tabContainer, fragment.childFragmentManager) {

    private var currentPosition: Int? = null
    private val config = tabConfig.config
    private var prevFragment: Fragment? = null

    override fun navigate(screen: NavigationScreen): Boolean {
        return when (screen) {
            is TabScreen -> {
                val tabPosition = config[screen.redirect.tag]
                    ?: throw IllegalArgumentException()

                if (currentPosition != tabPosition) {
                    tabSelectedListener.invoke(tabPosition)
                    currentPosition = tabPosition
                }

                val previousFragment = prevFragment
                previousFragment?.let { detach(previousFragment) }

                val fragmentFromManager =
                    fragment.childFragmentManager.findFragmentByTag(screen.redirect.tag)

                prevFragment = if (fragmentFromManager == null) {
                    super.navigate(screen.redirect)
                    screen.redirect.fragment
                } else {
                    attach(fragmentFromManager)
                    fragmentFromManager
                }
                true
            }
            else -> false
        }
    }

    private fun attach(fragment: Fragment) {
        fragment.parentFragmentManager
            .beginTransaction()
            .attach(fragment)
            .commit()
    }

    private fun detach(fragment: Fragment) {
        fragment.parentFragmentManager
            .beginTransaction()
            .detach(fragment)
            .commit()
    }

    fun restoreState() {
        currentPosition?.let { tabSelectedListener.invoke(it) }
    }
}