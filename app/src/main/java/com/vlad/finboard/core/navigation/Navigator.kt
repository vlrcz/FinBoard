package com.vlad.finboard.core.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.vlad.finboard.R
import com.vlad.finboard.core.navigation.screen.BackScreen
import com.vlad.finboard.core.navigation.screen.FragmentScreen
import com.vlad.finboard.core.navigation.screen.NavigationScreen

open class Navigator(
    private val activity: FragmentActivity,
    private val containerId: Int = R.id.container,
    private val fragmentManager: FragmentManager = activity.supportFragmentManager
) {

    open fun navigate(screen: NavigationScreen): Boolean {
        return when (screen) {
            is FragmentScreen -> {
                if (fragmentManager.findFragmentByTag(screen.tag) == null) {
                    replace(containerId, screen.fragment, screen.tag)
                } else {
                    attach(screen.tag)
                }
                true
            }
            is BackScreen -> {
                fragmentManager.popBackStack()
                true
            }
            else -> false
        }
    }

    private fun replace(containerId: Int, fragment: Fragment, tag: String) {
        fragmentManager
            .beginTransaction()
            .setPrimaryNavigationFragment(fragment)
            .addToBackStack(tag)
            .replace(containerId, fragment, tag)
            .commit()
    }

    open fun attach(tag: String) {
        val fragment = fragmentManager.findFragmentByTag(tag) ?: return
        fragmentManager
            .beginTransaction()
            .attach(fragment)
            .commit()
    }

    open fun detach(tag: String) {
        val fragment = fragmentManager.findFragmentByTag(tag) ?: return
        fragmentManager
            .beginTransaction()
            .detach(fragment)
            .commit()
    }
}

fun Fragment.navigate(screen: NavigationScreen) {
    var parent = parentFragment
    while (parent != null) {
        if (parent is NavigatorHolder) {
            val isSuccess = parent.navigator().navigate(screen)
            if (isSuccess) return
        }
        parent = parent.parentFragment
    }

    val activity = activity
    if (activity is NavigatorHolder) {
        activity.navigator().navigate(screen)
    }
}