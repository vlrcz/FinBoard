package com.vlad.finboard.feature.finances.list

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vlad.finboard.R
import com.vlad.finboard.app.appComponent
import com.vlad.finboard.core.navigation.NavigationConstants.ADD
import com.vlad.finboard.core.navigation.navigate
import com.vlad.finboard.core.navigation.screen.FragmentScreen
import com.vlad.finboard.databinding.FragmentFinancesBinding
import com.vlad.finboard.di.ViewModelFactory
import com.vlad.finboard.feature.finances.FinancesConstants.TYPE
import com.vlad.finboard.feature.finances.adapter.FinanceListAdapter
import com.vlad.finboard.feature.finances.detail.FinancesDetailFragment
import com.vlad.finboard.feature.finances.detail.FinancesDetailViewModel
import com.vlad.finboard.feature.finances.list.di.DaggerFinancesListComponent
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.flow.collect

class FinancesFragment : Fragment(R.layout.fragment_finances) {

    companion object {
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
        detailViewModel.restoreStateFromFinanceModel(it)
        navigate(FragmentScreen(FinancesDetailFragment(), ADD))
    }

    @Inject
    lateinit var detailViewModelProvider: Provider<FinancesDetailViewModel>
    private val detailViewModel: FinancesDetailViewModel by activityViewModels { ViewModelFactory { detailViewModelProvider.get() } }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerFinancesListComponent
            .factory()
            .create(context.appComponent)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = requireArguments().getString(TYPE)
        if (type != null) {
            viewModel.firstLoad(type)
            detailViewModel.setType(type)
        }
        bindViewModel()
        initList()
        bindOpenFinancesDetailBtn()
        refreshListAfterDetail()
    }

    private fun refreshListAfterDetail() {
        lifecycleScope.launchWhenStarted {
            detailViewModel.detailFlow.collect {
                if (it.isSaveSuccess) {
                    viewModel.refresh()
                    binding.financesList.scrollToPosition(0)
                }
            }
        }
    }

    private fun bindViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.pagingState.collect {
                binding.progressBar.isVisible = it.loadingPage
                financeListAdapter.submitList(it.itemsList)
                binding.emptyListTextView.isVisible = it.itemsList.isEmpty()
            }
        }
    }

    private fun bindOpenFinancesDetailBtn() {
        binding.openFinancesDetail.setOnClickListener {
            detailViewModel.resetState()
            navigate(FragmentScreen(FinancesDetailFragment(), ADD))
        }
    }

    private fun initList() {
        with(binding.financesList) {
            adapter = financeListAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        viewModel.loadMore()
                    }
                }
            })
        }
    }
}