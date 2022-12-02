package com.example.movies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.adapter.MovieListAdapter
import com.example.movies.adapter.MoviesLoadStateAdapter
import com.example.movies.databinding.FragmentHomeBinding
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModel<HomeViewModel>()
    private val movieListAdapter = MovieListAdapter { _, movie ->
        findNavController().navigate(HomeFragmentDirections.actionToMovieDetails(movie))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            fabScrollUp.hide()

            toolbar.setOnMenuItemClickListener(this@HomeFragment)

            refresher.setOnRefreshListener(movieListAdapter::refresh)

            movieList.adapter = movieListAdapter.withLoadStateHeaderAndFooter(
                header = MoviesLoadStateAdapter(movieListAdapter::retry),
                footer = MoviesLoadStateAdapter(movieListAdapter::retry)
            )

            setOnScrollToTop { movieList.smoothScrollToPosition(0) }

            movieList.addOnScrollListener(MoviesListScrollListener())

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    viewModel.dataFlow.collectLatest(movieListAdapter::submitData)
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    viewModel.sortFlow.collectLatest { movieListAdapter.refresh() }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    movieListAdapter.loadStateFlow.collectLatest { states ->
                        refresher.isRefreshing =
                            states.refresh == LoadState.Loading || states.prepend == LoadState.Loading
                    }
                }
            }
        }
    }

    inner class MoviesListScrollListener : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            if (layoutManager.findFirstVisibleItemPosition() > 7) {
                binding.fabScrollUp.show()
            } else {
                binding.fabScrollUp.hide()
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_search -> findNavController().navigate(HomeFragmentDirections.actionToSearch())
            R.id.nav_sort -> findNavController().navigate(HomeFragmentDirections.actionToNavSort())
        }
        return true
    }
}