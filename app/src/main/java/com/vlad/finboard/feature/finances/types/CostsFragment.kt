package com.vlad.finboard.feature.finances.types

import com.vlad.finboard.databinding.FragmentCostsBinding
import com.vlad.finboard.feature.finances.FinancesFragment
import com.vlad.finboard.feature.finances.types.FinancesType.COSTS

class CostsFragment : FinancesFragment<FragmentCostsBinding>(
    FragmentCostsBinding::inflate,
    COSTS.toString()
) {

    override fun FragmentCostsBinding.initialize() {
        this@CostsFragment.openFinancesDetail = binding.openFinancesDetail
        this@CostsFragment.financesList = binding.financesList
    }
}