package com.vlad.finboard.feature.finances

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vlad.finboard.R
import com.vlad.finboard.databinding.FragmentIncomeBinding

class IncomeFragment : FinancesFragment(R.layout.fragment_income) {

    private val binding: FragmentIncomeBinding by viewBinding(FragmentIncomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.openDetailFragment(binding.openFinancesDetail)
    }
}