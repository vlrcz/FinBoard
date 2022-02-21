package com.vlad.finboard.core.tab

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import com.vlad.finboard.R
import com.vlad.finboard.feature.finances.types.FinancesType.COSTS
import com.vlad.finboard.feature.finances.types.FinancesType.INCOME
import com.vlad.finboard.core.navigation.Navigator
import com.vlad.finboard.core.navigation.NavigatorHolder
import com.vlad.finboard.core.navigation.TabFragmentNavigator
import com.vlad.finboard.core.navigation.screen.FragmentScreen
import com.vlad.finboard.core.navigation.screen.TabScreen
import com.vlad.finboard.databinding.FragmentTabBinding
import com.vlad.finboard.feature.finances.types.CostsFragment
import com.vlad.finboard.feature.finances.types.IncomeFragment

class TabFragment : Fragment(R.layout.fragment_tab), NavigatorHolder {

    private val binding: FragmentTabBinding by viewBinding(FragmentTabBinding::bind)
    lateinit var navigator: TabFragmentNavigator

    override fun navigator(): Navigator {
        return navigator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigator = TabFragmentNavigator(
            this,
            TabConfig(
                mapOf(
                    COSTS.toString() to 0,
                    INCOME.toString() to 1
                )
            )
        ) {
            if (view == null) return@TabFragmentNavigator

            val tabLayout = binding.tabLayout
            tabLayout.selectTab(tabLayout.getTabAt(it))
        }
        if (savedInstanceState == null) {
            navigator.navigate(TabScreen(FragmentScreen(CostsFragment(), COSTS.toString())))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindOnBackPressedCallback()
        bindTabLayout()

        if (savedInstanceState == null) {
            navigator.restoreState()
        }
    }

    private fun bindTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: Tab?) {
                when (tab?.position) {
                    0 -> navigator.navigate(
                        TabScreen(
                            FragmentScreen(
                                CostsFragment(),
                                COSTS.toString()
                            )
                        )
                    )
                    1 -> navigator.navigate(
                        TabScreen(
                            FragmentScreen(
                                IncomeFragment(),
                                INCOME.toString()
                            )
                        )
                    )
                }
            }

            override fun onTabUnselected(tab: Tab?) {}
            override fun onTabReselected(tab: Tab?) {}
        })
    }

    private fun bindOnBackPressedCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
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