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
import com.example.movies.adapter.TheMovieListAdapter
import com.example.movies.adapter.TheMoviesLoadStateAdapter
import com.example.movies.databinding.FragmentHomeBinding
import com.example.movies.ui.moviedetails.DataType
import com.example.movies.utils.extensions.collectOnCreated
import com.google.android.material.transition.MaterialSharedAxis
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModel<HomeViewModel>()
    private val movieListAdapter = TheMovieListAdapter { _, movie ->
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        findNavController().navigate(
            HomeFragmentDirections.actionToMovieDetails(movie.id, DataType.MOVIE)
        )
    }
    private val tvListAdapter = TheMovieListAdapter { _, movie ->
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        findNavController().navigate(
            HomeFragmentDirections.actionToMovieDetails(movie.id, DataType.TV_SHOW)
        )
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

            refresher.setOnRefreshListener {
                movieListAdapter.refresh()
                tvListAdapter.refresh()
            }

            movieList.adapter = movieListAdapter.withLoadStateHeaderAndFooter(
                header = TheMoviesLoadStateAdapter(movieListAdapter::retry),
                footer = TheMoviesLoadStateAdapter(movieListAdapter::retry)
            )

            tvList.adapter = tvListAdapter.withLoadStateHeaderAndFooter(
                header = TheMoviesLoadStateAdapter(movieListAdapter::retry),
                footer = TheMoviesLoadStateAdapter(movieListAdapter::retry)
            )

            fabScrollUp.setOnClickListener { movieList.smoothScrollToPosition(0) }

            movieList.addOnScrollListener(MoviesListScrollListener())

            viewModel.moviesDataFlow.collectOnCreated(
                viewLifecycleOwner,
                movieListAdapter::submitData
            )

            viewModel.tvDataFlow.collectOnCreated(viewLifecycleOwner, tvListAdapter::submitData)

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
            R.id.nav_translations -> findNavController().navigate(HomeFragmentDirections.actionToNavTranslations())
        }
        return true
    }
}