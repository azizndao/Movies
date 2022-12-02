package com.example.movies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.adapter.MovieListAdapter
import com.example.movies.adapter.MoviesLoadStateAdapter
import com.example.movies.databinding.FragmentHomeBinding
import com.example.movies.utils.extensions.collectOnCreated
import com.google.android.material.transition.MaterialSharedAxis
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModel<HomeViewModel>()
    private val movieListAdapter = MovieListAdapter { _, movie ->
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        findNavController().navigate(HomeFragmentDirections.actionToMovieDetails(movie))
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

            viewModel.dataFlow.collectOnCreated(viewLifecycleOwner, movieListAdapter::submitData)

            viewModel.sortFlow.collectOnCreated(viewLifecycleOwner) { movieListAdapter.refresh() }

            movieListAdapter.loadStateFlow.collectOnCreated(viewLifecycleOwner) { states ->
                refresher.isRefreshing =
                    states.refresh == LoadState.Loading || states.prepend == LoadState.Loading
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
            R.id.nav_search -> {
                exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
                reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
                findNavController().navigate(HomeFragmentDirections.actionToSearch())
            }

            R.id.nav_sort -> findNavController().navigate(HomeFragmentDirections.actionToNavSort())
        }
        return true
    }
}