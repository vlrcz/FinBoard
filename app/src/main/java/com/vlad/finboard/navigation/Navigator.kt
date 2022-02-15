package com.vlad.finboard.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.vlad.finboard.R
import com.vlad.finboard.navigation.screen.FragmentScreen
import com.vlad.finboard.navigation.screen.NavigationScreen

open class Navigator(
    private val activity: FragmentActivity,
    private val containerId: Int = R.id.container,
    private val fragmentManager: FragmentManager = activity.supportFragmentManager
) {

    fun back() {
        fragmentManager.popBackStack()
    }

    open fun navigate(screen: NavigationScreen): Boolean {
        return when (screen) {
            is FragmentScreen -> {
                fragmentManager.replace(containerId, screen.fragment, screen.tag)
                true
            }
            else -> false
        }
    }

    private fun FragmentManager.replace(containerId: Int, fragment: Fragment, tag: String) {
        with(this) {
            beginTransaction()
                .setPrimaryNavigationFragment(fragment)
                .addToBackStack(tag)
                .replace(containerId, fragment)
                .commit()
        }
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