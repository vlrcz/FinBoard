package com.vlad.finboard.tab

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vlad.finboard.R
import com.vlad.finboard.databinding.FragmentIncomeBinding
import com.vlad.finboard.navigation.navigate
import com.vlad.finboard.navigation.screen.FragmentScreen

class IncomeFragment: Fragment(R.layout.fragment_income) {

    companion object {
        const val ADD = "add"
    }

    private val binding: FragmentIncomeBinding by viewBinding(FragmentIncomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            navigate(FragmentScreen(AddNoteFragment(), ADD))
        }
    }
}