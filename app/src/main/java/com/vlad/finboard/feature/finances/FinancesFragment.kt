package com.vlad.finboard.feature.finances

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vlad.finboard.app.appComponent
import com.vlad.finboard.core.navigation.navigate
import com.vlad.finboard.core.navigation.screen.FragmentScreen
import com.vlad.finboard.di.ViewModelFactory
import com.vlad.finboard.feature.finances.FinancesConstants.ADD
import com.vlad.finboard.feature.finances.detail.FinancesDetailFragment
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.flow.collect

open class FinancesFragment<T : ViewBinding>(
    private val inflateMethod: (LayoutInflater, ViewGroup?, Boolean) -> T,
    private val type: String
) : Fragment() {

    private var _binding: T? = null
    val binding: T get() = _binding!!
    var openFinancesDetail: FloatingActionButton? = null
    var notesList: RecyclerView? = null

    private val notesListAdapter = NotesListAdapter() {

    }

    @Inject
    lateinit var viewModelProvider: Provider<FinancesViewModel>
    private val viewModel: FinancesViewModel by viewModels { ViewModelFactory { viewModelProvider.get() } }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this as FinancesFragment<ViewBinding>)
    }

    open fun T.initialize() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateMethod.invoke(inflater, container, false)
        binding.initialize()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        openDetailFragment()
        initList()
    }

    private fun bindViewModel() {
        viewModel.fetchNotes(type)
        lifecycleScope.launchWhenStarted {
            viewModel.notes.collect {
                notesListAdapter.submitList(it)
            }
        }
    }

    private fun openDetailFragment() {
        openFinancesDetail?.setOnClickListener {
            navigate(FragmentScreen(FinancesDetailFragment.newInstance(type), ADD))
        }
    }

    private fun initList() {
        with(notesList) {
            this?.adapter = notesListAdapter
            this?.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }
}