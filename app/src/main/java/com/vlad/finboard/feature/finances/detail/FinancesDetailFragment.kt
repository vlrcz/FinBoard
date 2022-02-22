package com.vlad.finboard.feature.finances.detail

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vlad.finboard.R
import com.vlad.finboard.R.string
import com.vlad.finboard.app.appComponent
import com.vlad.finboard.core.navigation.navigate
import com.vlad.finboard.core.navigation.screen.BackScreen
import com.vlad.finboard.databinding.FragmentFinancesDetailBinding
import com.vlad.finboard.di.ViewModelFactory
import com.vlad.finboard.feature.finances.FinanceModel
import com.vlad.finboard.feature.finances.FinancesConstants.DETAIL
import com.vlad.finboard.hideSoftKeyboard
import com.vlad.finboard.toast
import java.text.SimpleDateFormat
import java.util.Locale
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
    private var categoryId: Int? = null
    private var financeDate: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindSaveButton()
        onEditTextFocusChanged()
        restoreFinanceDetailState()
    }

    private fun restoreFinanceDetailState() {
        val model = requireArguments().getParcelable(DETAIL) as? FinanceModel
        if (model != null) {
            categoryId = model.categoryId
            financeDate = model.date
            binding.sumEditText.setText(model.sum)
        }
    }

    private fun bindSaveButton() {
        binding.saveBtn.setOnClickListener {
            categoryId = 1 //todo убрать после добавления адаптера
            val catId = categoryId
            financeDate = millisToDate(System.currentTimeMillis())
            val date = financeDate
            val sum = binding.sumEditText.text?.toString()
            if (catId != null && sum != null && date != null) {
                viewModel.saveFinance(
                    categoryId = catId,
                    sum = sum,
                    date = date
                )
                navigate(BackScreen())
            } else {
                toast(getString(string.fill_fields))
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

    private fun millisToDate(millis: Long): String {
        val targetFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)
        return targetFormat.format(millis)
    }
}