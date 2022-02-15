package com.vlad.finboard.tab

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vlad.finboard.R
import com.vlad.finboard.databinding.FragmentTabBinding
import com.vlad.finboard.navigation.Navigator
import com.vlad.finboard.navigation.NavigatorHolder
import com.vlad.finboard.navigation.TabFragmentNavigator

class TabFragment : Fragment(R.layout.fragment_tab), NavigatorHolder {

    private val binding: FragmentTabBinding by viewBinding(FragmentTabBinding::bind)
    lateinit var navigator: Navigator

    override fun navigator(): Navigator {
        return navigator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigator = TabFragmentNavigator(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindOnBackPressedCallback()
    }

    private fun bindOnBackPressedCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (childFragmentManager.backStackEntryCount > 1) {
                        navigator.back()
                    } else {
                        requireActivity().finish()
                    }
                }
            })
    }
}