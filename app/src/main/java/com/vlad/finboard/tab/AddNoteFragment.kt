package com.vlad.finboard.tab

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vlad.finboard.R
import com.vlad.finboard.databinding.FragmentAddNoteBinding

class AddNoteFragment: Fragment(R.layout.fragment_add_note) {

    private val binding: FragmentAddNoteBinding by viewBinding(FragmentAddNoteBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}