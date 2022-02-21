package com.vlad.finboard.feature.finances

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vlad.finboard.R
import com.vlad.finboard.databinding.FragmentCostsBinding

class CostsFragment : FinancesFragment(R.layout.fragment_costs) {

    private val binding: FragmentCostsBinding by viewBinding(FragmentCostsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.openDetailFragment(binding.openFinancesDetail)
    }
}