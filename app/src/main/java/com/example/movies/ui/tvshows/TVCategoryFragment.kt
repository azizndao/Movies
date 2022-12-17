package com.example.movies.ui.tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.adapter.TheMovieGridAdapter
import com.example.movies.adapter.TheMoviesLoadStateAdapter
import com.example.movies.databinding.FragmentCategoryBinding
import com.example.movies.ui.moviedetails.DataType
import com.example.movies.utils.extensions.collectOnCreated
import org.koin.androidx.viewmodel.ext.android.viewModel

class TVCategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private val viewModel: TvShowViewModel by viewModel(ownerProducer = { requireParentFragment() })
    private lateinit var movieItemsAdapter: TheMovieGridAdapter
    private lateinit var layoutManager: GridLayoutManager
    private val dataFlow by lazy {
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
            movieItemsAdapter = TheMovieGridAdapter { _, tv ->
                findNavController().navigate(
                    TvShowFragmentDirections.actionToTvShowDetails(tv.id, DataType.TV_SHOW)
                )
            }

            items.layoutManager = GridLayoutManager(requireContext(), 2).also { layoutManager = it }
            items.adapter = movieItemsAdapter.withLoadStateHeaderAndFooter(
                TheMoviesLoadStateAdapter(movieItemsAdapter::retry),
                TheMoviesLoadStateAdapter(movieItemsAdapter::retry),
            )
            dataFlow.collectOnCreated(viewLifecycleOwner) { data ->
                movieItemsAdapter.submitData(data)
            }
        }
    }

    companion object {
        const val ID = "id"
    }
}
