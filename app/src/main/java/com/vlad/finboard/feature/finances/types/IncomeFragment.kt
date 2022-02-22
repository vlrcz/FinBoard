package com.vlad.finboard.feature.finances.types

import com.vlad.finboard.databinding.FragmentIncomeBinding
import com.vlad.finboard.feature.finances.FinancesFragment
import com.vlad.finboard.feature.finances.types.FinancesType.INCOME

class IncomeFragment : FinancesFragment<FragmentIncomeBinding>(
    FragmentIncomeBinding::inflate,
    INCOME.toString()
) {

    override fun FragmentIncomeBinding.initialize() {
        this@IncomeFragment.openFinancesDetail = binding.openFinancesDetail
        this@IncomeFragment.financesList = binding.financesList
    }
}