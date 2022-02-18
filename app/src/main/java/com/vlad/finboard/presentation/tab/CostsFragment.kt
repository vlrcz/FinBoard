package com.vlad.finboard.presentation.tab

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vlad.finboard.R
import com.vlad.finboard.databinding.FragmentCostsBinding
import com.vlad.finboard.navigation.navigate
import com.vlad.finboard.navigation.screen.FragmentScreen
import com.vlad.finboard.presentation.notes.save.SaveNoteFragment

class CostsFragment: Fragment(R.layout.fragment_costs) {

    companion object {
        const val ADD = "add"
        const val COSTS = "costs"
    }

    private val binding: FragmentCostsBinding by viewBinding(FragmentCostsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            navigate(FragmentScreen(SaveNoteFragment.newInstance(COSTS), ADD))
        }
    }
}