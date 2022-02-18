package com.vlad.finboard.presentation.notes.save

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vlad.finboard.R
import com.vlad.finboard.app.appComponent
import com.vlad.finboard.databinding.FragmentSaveNoteBinding
import com.vlad.finboard.di.ViewModelFactory
import com.vlad.finboard.hideSoftKeyboard
import com.vlad.finboard.navigation.back
import com.vlad.finboard.presentation.categories.CategoryListAdapter
import com.vlad.finboard.toast
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.flow.collect


class SaveNoteFragment : Fragment(R.layout.fragment_save_note) {

    companion object {
        const val TAB = "tab"

        fun newInstance(flag: String): SaveNoteFragment {
            return SaveNoteFragment().apply {
                arguments = bundleOf(TAB to flag)
            }
        }
    }

    @Inject
    lateinit var viewModelProvider: Provider<SaveNoteViewModel>
    private val viewModel: SaveNoteViewModel by viewModels { ViewModelFactory { viewModelProvider.get() } }
    private val binding: FragmentSaveNoteBinding by viewBinding(FragmentSaveNoteBinding::bind)
    private var categoryId: Int? = null
    private val categoryListAdapter = CategoryListAdapter() {
        categoryId = it.id
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCategoriesList()
        saveNote()
        onEditTextFocusChanged()
    }

    private fun onEditTextFocusChanged() {
        binding.sumEditText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                requireActivity().hideSoftKeyboard(v)
            }
        }
    }

    private fun saveNote() {
        binding.saveBtn.setOnClickListener {
            val sum = binding.sumEditText.text?.toString()
            val date = millisToDate(System.currentTimeMillis())
            if (categoryId != null && sum != null) {
                viewModel.saveNote(categoryId = categoryId!!, sum = sum, date = date)
                back()
            } else {
                toast(getString(R.string.fill_fields))
            }
        }
    }

    private fun initCategoriesList() {
        val dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.divider_categories, null)
        if (drawable != null) {
            dividerItemDecoration.setDrawable(drawable)
        }

        with(binding.categoriesList) {
            adapter = categoryListAdapter
            layoutManager = GridLayoutManager(requireContext(), 4)
            addItemDecoration(dividerItemDecoration)
        }

        val flag = requireArguments().getString(TAB)
        if (flag != null) {
            viewModel.fetchCategories(flag)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.categories.collect {
                categoryListAdapter.submitList(it)
            }
        }
    }

    private fun millisToDate(millis: Long): String {
        val targetFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)
        return targetFormat.format(millis)
    }
}