package com.vlad.finboard.feature.finances.detail

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vlad.finboard.R
import com.vlad.finboard.app.appComponent
import com.vlad.finboard.core.navigation.navigate
import com.vlad.finboard.core.navigation.screen.BackScreen
import com.vlad.finboard.databinding.FragmentFinancesDetailBinding
import com.vlad.finboard.di.ViewModelFactory
import com.vlad.finboard.feature.finances.FinancesConstants.DETAIL
import com.vlad.finboard.feature.finances.detail.di.DaggerFinancesDetailComponent
import com.vlad.finboard.feature.finances.model.FinanceModel
import com.vlad.finboard.hideSoftKeyboard
import com.vlad.finboard.toast
import javax.inject.Inject
import javax.inject.Provider

class FinancesDetailFragment : Fragment(R.layout.fragment_finances_detail) {

    companion object {
        fun newInstance(finance: FinanceModel?): FinancesDetailFragment {
            return FinancesDetailFragment().apply {
                arguments = bundleOf(DETAIL to finance)
            }
        }
    }

    @Inject
    lateinit var viewModelProvider: Provider<FinancesDetailViewModel>
    private val viewModel: FinancesDetailViewModel by viewModels { ViewModelFactory { viewModelProvider.get() } }
    private val binding: FragmentFinancesDetailBinding by viewBinding(FragmentFinancesDetailBinding::bind)
    private var _categoryId: Int? = null
    private var _createAt: Long? = null
    private var _financeId: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerFinancesDetailComponent
            .factory()
            .create(context.appComponent)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindSaveButton()
        onEditTextFocusChanged()
        fillViewFromArguments()
    }

    private fun fillViewFromArguments() {
        if (arguments == null) return
        val model = requireArguments().getParcelable(DETAIL) as? FinanceModel ?: return
        _categoryId = model.categoryId
        _createAt = model.createAt.dateMillis
        _financeId = model.id
        binding.sumEditText.setText(model.sum.toString())
    }

    private fun bindSaveButton() {
        binding.saveBtn.setOnClickListener {
            _categoryId = 1 //todo убрать после добавления адаптера
            val categoryId = _categoryId
            val createAt = _createAt ?: System.currentTimeMillis()
            val updateAt = System.currentTimeMillis()
            val financeId = _financeId
            val sum = binding.sumEditText.text?.toString()?.toDouble()
            if (categoryId != null && sum != null) {
                viewModel.saveFinance(
                    financeId = financeId,
                    categoryId = categoryId,
                    sum = sum,
                    createAt = createAt,
                    updateAt = updateAt
                )
                navigate(BackScreen())
            } else {
                toast(getString(R.string.fill_fields))
            }
        }
    }

    private fun onEditTextFocusChanged() {
        binding.sumEditText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                requireActivity().hideSoftKeyboard(v)
            }
        }
    }
}