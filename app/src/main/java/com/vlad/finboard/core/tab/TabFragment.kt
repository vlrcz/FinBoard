package com.vlad.finboard.core.tab

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import com.vlad.finboard.R
import com.vlad.finboard.app.appComponent
import com.vlad.finboard.core.navigation.NavigationConstants
import com.vlad.finboard.core.navigation.Navigator
import com.vlad.finboard.core.navigation.NavigatorHolder
import com.vlad.finboard.core.navigation.TabFragmentNavigator
import com.vlad.finboard.core.navigation.screen.BackScreen
import com.vlad.finboard.core.navigation.screen.FragmentScreen
import com.vlad.finboard.core.navigation.screen.TabScreen
import com.vlad.finboard.core.tab.di.DaggerTabComponent
import com.vlad.finboard.databinding.FragmentTabBinding
import com.vlad.finboard.di.ViewModelFactory
import com.vlad.finboard.feature.charts.PieChartView
import com.vlad.finboard.feature.finances.FinancesConstants
import com.vlad.finboard.feature.finances.list.FinancesFragment
import com.vlad.finboard.feature.finances.types.FinancesType.COSTS
import com.vlad.finboard.feature.finances.types.FinancesType.INCOME
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class TabFragment : Fragment(R.layout.fragment_tab), NavigatorHolder {

    companion object {
        const val POSITION = "tab position"
    }

    private val binding: FragmentTabBinding by viewBinding(FragmentTabBinding::bind)
    lateinit var navigator: TabFragmentNavigator
    private val tabConfig = TabConfig(mapOf(NavigationConstants.COSTS to 0, NavigationConstants.INCOME to 1))
    lateinit var pieView: PieChartView
    @Inject
    lateinit var viewModelProvider: Provider<TabViewModel>
    private val viewModel: TabViewModel by viewModels { ViewModelFactory { viewModelProvider.get() } }

    override fun navigator(): Navigator {
        return navigator
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerTabComponent
            .factory()
            .create(context.appComponent)
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigator = TabFragmentNavigator(this, tabConfig) {
            if (view == null) return@TabFragmentNavigator

            val tabLayout = binding.tabLayout
            tabLayout.selectTab(tabLayout.getTabAt(it))
        }
        if (savedInstanceState == null) {
            navigateTab(FragmentScreen(FinancesFragment.newInstance(COSTS.name), NavigationConstants.COSTS))
            viewModel.getPieChartMapByType(COSTS.name)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindOnBackPressedCallback()
        bindTabLayout()

        val tabPosition = savedInstanceState?.getInt(POSITION)
        navigator.restoreState(tabPosition)

        addPieChartView()
        refreshPieChartAfterDetail()
        bindViewModel()
    }

    private fun bindViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.pieChartFlow.collect {
                if (it.isNotEmpty()) {
                    binding.financesPieChart.isVisible = true
                    pieView.apply {
                        setValues(it)
                        invalidate()
                    }
                } else {
                    binding.financesPieChart.isVisible = false
                }
            }
        }
    }

    private fun addPieChartView() {
        pieView = PieChartView(requireContext())
        binding.financesPieChart.addView(pieView)
    }

    private fun refreshPieChartAfterDetail() {
        requireActivity().supportFragmentManager.setFragmentResultListener(FinancesConstants.TAB, this) { _, _ ->
            val type = if (binding.tabLayout.selectedTabPosition == 0) COSTS.name else INCOME.name
            viewModel.getPieChartMapByType(type)
            pieView.invalidate()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(POSITION, binding.tabLayout.selectedTabPosition)
    }

    private fun navigateTab(screen: FragmentScreen) {
        navigator.navigate(TabScreen(screen))
    }

    private fun bindTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: Tab?) {
                when (tab?.position) {
                    0 -> {
                        navigateTab(FragmentScreen(FinancesFragment.newInstance(COSTS.name), NavigationConstants.COSTS))
                        viewModel.getPieChartMapByType(COSTS.name)
                    }
                    1 -> {
                        navigateTab(FragmentScreen(FinancesFragment.newInstance(INCOME.name), NavigationConstants.INCOME))
                        viewModel.getPieChartMapByType(INCOME.name)
                    }
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
                        navigator.navigate(BackScreen())
                    } else {
                        requireActivity().finish()
                    }
                }
            })
    }
}