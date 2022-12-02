package com.example.movies.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePaddingRelative
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.movies.adapter.MovieListAdapter
import com.example.movies.adapter.MoviesLoadStateAdapter
import com.example.movies.databinding.FragmentSearchBinding
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModel()
    private lateinit var binding: FragmentSearchBinding

    private val searchResultAdapter = MovieListAdapter { _, movie ->
        findNavController().navigate(SearchFragmentDirections.actionToMovieDetails(movie))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            inputSearch.doAfterTextChanged {
                viewModel.updateQuery(it.toString())
            }

            ViewCompat.setOnApplyWindowInsetsListener(container) { _, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                searchResults.updatePaddingRelative(
                    start = insets.left, top = 0, end = insets.right, bottom = insets.bottom
                )
                toolbar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = insets.top
                }
                WindowInsetsCompat.CONSUMED
            }

            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

            searchResults.adapter = searchResultAdapter.withLoadStateHeaderAndFooter(
                header = MoviesLoadStateAdapter(searchResultAdapter::retry),
                footer = MoviesLoadStateAdapter(searchResultAdapter::retry)
            )

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    viewModel.dataFlow.collectLatest(searchResultAdapter::submitData)
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    searchResultAdapter.loadStateFlow.collect { states ->
                        progress.isVisible = states.refresh == LoadState.Loading
                    }
                }
            }
        }
    }
}