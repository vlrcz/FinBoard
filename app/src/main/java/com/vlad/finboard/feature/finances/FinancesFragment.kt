package com.vlad.finboard.feature.finances

import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vlad.finboard.core.navigation.navigate
import com.vlad.finboard.core.navigation.screen.FragmentScreen
import com.vlad.finboard.feature.finances.FinancesConstants.ADD
import com.vlad.finboard.feature.finances.detail.FinancesDetailFragment

open class FinancesFragment(layout: Int) : Fragment(layout) {

    open fun openDetailFragment(fab: FloatingActionButton, type: String) {
        fab.setOnClickListener {
            navigate(FragmentScreen(FinancesDetailFragment.newInstance(type), ADD))
        }
    }
}