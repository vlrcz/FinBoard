package com.vlad.finboard.feature.finances.types

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vlad.finboard.R
import com.vlad.finboard.databinding.FragmentIncomeBinding
import com.vlad.finboard.feature.finances.FinancesFragment
import com.vlad.finboard.feature.finances.types.FinancesType.INCOME

class IncomeFragment : FinancesFragment(R.layout.fragment_income) {

    private val binding: FragmentIncomeBinding by viewBinding(FragmentIncomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.openDetailFragment(binding.openFinancesDetail, INCOME.toString())
    }
}