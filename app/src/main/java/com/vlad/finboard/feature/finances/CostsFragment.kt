package com.vlad.finboard.feature.finances

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vlad.finboard.R
import com.vlad.finboard.databinding.FragmentCostsBinding
import com.vlad.finboard.core.navigation.navigate
import com.vlad.finboard.core.navigation.screen.FragmentScreen
import com.vlad.finboard.feature.finances.FinancesConstants.ADD
import com.vlad.finboard.feature.finances.detail.FinancesDetailFragment

class CostsFragment: Fragment(R.layout.fragment_costs) {

    private val binding: FragmentCostsBinding by viewBinding(FragmentCostsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            navigate(FragmentScreen(FinancesDetailFragment(), ADD))
        }
    }
}