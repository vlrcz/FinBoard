package com.vlad.finboard.feature.finances

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vlad.finboard.R
import com.vlad.finboard.app.appComponent
import com.vlad.finboard.core.navigation.NavigationConstants.ADD
import com.vlad.finboard.core.navigation.navigate
import com.vlad.finboard.core.navigation.screen.FragmentScreen
import com.vlad.finboard.databinding.FragmentFinancesBinding
import com.vlad.finboard.di.ViewModelFactory
import com.vlad.finboard.feature.finances.detail.FinancesDetailFragment
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.flow.collect

class FinancesFragment : Fragment(R.layout.fragment_finances) {

    companion object {
        private const val TYPE = "type"
        fun newInstance(type: String): FinancesFragment {
            return FinancesFragment().apply {
                arguments = bundleOf(TYPE to type)
            }
        }
    }

    @Inject
    lateinit var viewModelProvider: Provider<FinancesViewModel>
    private val viewModel: FinancesViewModel by viewModels { ViewModelFactory { viewModelProvider.get() } }
    private val binding: FragmentFinancesBinding by viewBinding(FragmentFinancesBinding::bind)
    private val financeListAdapter = FinanceListAdapter() {
        navigate(FragmentScreen(FinancesDetailFragment.newInstance(it), ADD))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = requireArguments().getString(TYPE)
        if (type != null) {
            bindViewModel(type)
        }
        bindOpenFinancesDetailBtn()
        initList()
    }

    private fun bindViewModel(type: String) {
        viewModel.fetchFinances(type)
        lifecycleScope.launchWhenStarted {
            viewModel.finances.collect {
                financeListAdapter.submitList(it)
            }
        }
    }

    private fun bindOpenFinancesDetailBtn() {
        binding.openFinancesDetail.setOnClickListener {
            navigate(FragmentScreen(FinancesDetailFragment(), ADD))
        }
    }

    private fun initList() {
        with(binding.financesList) {
            adapter = financeListAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }
}