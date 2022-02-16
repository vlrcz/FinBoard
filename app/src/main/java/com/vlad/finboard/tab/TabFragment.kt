package com.vlad.finboard.tab

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import com.vlad.finboard.R
import com.vlad.finboard.databinding.FragmentTabBinding
import com.vlad.finboard.navigation.Navigator
import com.vlad.finboard.navigation.NavigatorHolder
import com.vlad.finboard.navigation.TabFragmentNavigator
import com.vlad.finboard.navigation.screen.FragmentScreen
import com.vlad.finboard.navigation.screen.TabScreen

class TabFragment : Fragment(R.layout.fragment_tab), NavigatorHolder {

    companion object {
        const val COSTS = "costs"
        const val INCOME = "income"
    }

    private val binding: FragmentTabBinding by viewBinding(FragmentTabBinding::bind)
    lateinit var navigator: Navigator

    override fun navigator(): Navigator {
        return navigator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigator = TabFragmentNavigator(this)
        if (savedInstanceState == null) {
            navigator.navigate(TabScreen(FragmentScreen(CostsFragment(), COSTS)))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindOnBackPressedCallback()
        bindTabLayout()
    }

    private fun bindTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: Tab?) {
                when (tab?.position) {
                    0 -> navigator.navigate(TabScreen(FragmentScreen(CostsFragment(), COSTS)))
                    1 -> navigator.navigate(TabScreen(FragmentScreen(IncomeFragment(), INCOME)))
                }
            }
            override fun onTabUnselected(tab: Tab?) {}
            override fun onTabReselected(tab: Tab?) {}
        })
    }

    private fun bindOnBackPressedCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (childFragmentManager.backStackEntryCount > 1) {
                        navigator.back()
                    } else {
                        requireActivity().finish()
                    }
                }
            })
    }
}