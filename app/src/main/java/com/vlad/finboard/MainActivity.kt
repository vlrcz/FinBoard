package com.vlad.finboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vlad.finboard.presentation.tab.TabFragment
import com.vlad.finboard.navigation.screen.FragmentScreen
import com.vlad.finboard.navigation.Navigator
import com.vlad.finboard.navigation.NavigatorHolder

class MainActivity : AppCompatActivity(R.layout.activity_main), NavigatorHolder {

    companion object {
        const val TAB_FRAGMENT = "tab"
    }

    lateinit var navigator: Navigator

    override fun navigator(): Navigator {
        return navigator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigator = Navigator(this)

        if (savedInstanceState == null) {
            navigator.navigate(FragmentScreen(TabFragment(), TAB_FRAGMENT))
        }
    }
}