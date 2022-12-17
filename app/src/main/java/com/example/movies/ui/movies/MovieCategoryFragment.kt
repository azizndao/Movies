package com.example.movies.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.adapter.CardUiState
import com.example.movies.adapter.TheMovieGridAdapter
import com.example.movies.adapter.TheMoviesLoadStateAdapter
import com.example.movies.databinding.FragmentCategoryBinding
import com.example.movies.ui.moviedetails.DataType
import com.example.movies.utils.extensions.collectOnCreated
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieCategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private val viewModel: MoviesViewModel by viewModel(ownerProducer = { requireParentFragment() })
    private lateinit var movieItemsAdapter: TheMovieGridAdapter
    private lateinit var layoutManager: GridLayoutManager
    private val dataFlow: Flow<PagingData<CardUiState>> by lazy {
        val id = requireArguments().getInt(ID)
        viewModel.tabViews[id]!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            movieItemsAdapter = TheMovieGridAdapter { _, movie ->
                findNavController().navigate(MoviesFragmentDirections.actionToMovieDetails(movie.id, DataType.MOVIE))
            }

            items.layoutManager = GridLayoutManager(requireContext(), 2).also { layoutManager = it }
            items.adapter = movieItemsAdapter.withLoadStateHeaderAndFooter(
                TheMoviesLoadStateAdapter(movieItemsAdapter::retry),
                TheMoviesLoadStateAdapter(movieItemsAdapter::retry),
            )
            dataFlow.collectOnCreated(viewLifecycleOwner) { data ->
                movieItemsAdapter.submitData(
                    data
                )
            }
        }
    }

    companion object {
        const val ID = "id"
    }
}