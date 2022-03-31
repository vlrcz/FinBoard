package com.vlad.finboard.feature.finances.detail

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vlad.finboard.R
import com.vlad.finboard.app.appComponent
import com.vlad.finboard.core.navigation.navigate
import com.vlad.finboard.core.navigation.screen.BackScreen
import com.vlad.finboard.databinding.FragmentFinancesDetailBinding
import com.vlad.finboard.di.ViewModelFactory
import com.vlad.finboard.feature.categories.adapter.CategoriesListAdapter
import com.vlad.finboard.feature.finances.FinancesConstants
import com.vlad.finboard.feature.finances.FinancesConstants.DETAIL
import com.vlad.finboard.feature.finances.FinancesConstants.TAB
import com.vlad.finboard.feature.finances.detail.di.DaggerFinancesDetailComponent
import com.vlad.finboard.feature.finances.model.FinanceModel
import com.vlad.finboard.hideSoftKeyboard
import com.vlad.finboard.toast
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.flow.collect

class FinancesDetailFragment : Fragment(R.layout.fragment_finances_detail) {

    companion object {
        fun newInstance(finance: FinanceModel): FinancesDetailFragment {
            return FinancesDetailFragment().apply {
                arguments = bundleOf(DETAIL to finance)
            }
        }

        fun newInstance(type: String): FinancesDetailFragment {
            return FinancesDetailFragment().apply {
                arguments = bundleOf(FinancesConstants.TYPE to type)
            }
        }
    }

    @Inject
    lateinit var viewModelProvider: Provider<FinancesDetailViewModel>
    private val viewModel: FinancesDetailViewModel by viewModels { ViewModelFactory { viewModelProvider.get() } }
    private val binding: FragmentFinancesDetailBinding by viewBinding(FragmentFinancesDetailBinding::bind)
    private val categoryListAdapter = CategoriesListAdapter() { viewModel.clickedOnCategory(it.id) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerFinancesDetailComponent
            .factory()
            .create(context.appComponent)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEditTextFocusChangeListener()
        bindViewModel()
        initCategoriesList()

        val type = requireArguments().getString(FinancesConstants.TYPE)
        val model = requireArguments().getParcelable(DETAIL) as? FinanceModel

        if (type != null) {
            bindSaveButton(type)
            viewModel.fetchCategoriesByType(type)
        }
        if (model != null) {
            bindUpdateButton(model)
            binding.sumEditText.setText(model.sum.sumBigDecimal.toPlainString())
            viewModel.fillStateFromFinanceModel(model)
        }
    }

    private fun bindViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.categoriesFlow.collect {
                categoryListAdapter.submitList(it.categoriesList)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.saveSuccessFlow.collect {
                if (it) {
                    requireActivity().supportFragmentManager.setFragmentResult(DETAIL, Bundle())
                    requireActivity().supportFragmentManager.setFragmentResult(TAB, Bundle())
                    navigate(BackScreen())
                }
            }
        }
    }

    private fun bindSaveButton(type: String) {
        binding.saveBtn.setOnClickListener {
            val sum = binding.sumEditText.text.toString()
            if (sum.isNotBlank()) {
                viewModel.saveFinance(type = type, sum = sum)
            } else {
                toast(getString(R.string.fill_fields))
            }
        }
    }

    private fun bindUpdateButton(model: FinanceModel) {
        binding.saveBtn.setOnClickListener {
            val sum = binding.sumEditText.text.toString()
            if (sum.isNotBlank()) {
                viewModel.updateFinance(model = model, sum = sum)
            } else {
                toast(getString(R.string.fill_fields))
            }
        }
    }

    private fun setEditTextFocusChangeListener() {
        binding.sumEditText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                requireActivity().hideSoftKeyboard(v)
            }
        }
    }

    private fun initCategoriesList() {
        val dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.divider_categories, null)
        if (drawable != null) {
            dividerItemDecoration.setDrawable(drawable)
        }

        with(binding.categoriesList) {
            adapter = categoryListAdapter
            layoutManager = GridLayoutManager(requireContext(), 4)
            addItemDecoration(dividerItemDecoration)
        }
    }
}