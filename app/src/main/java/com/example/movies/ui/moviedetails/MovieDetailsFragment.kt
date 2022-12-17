package com.example.movies.ui.moviedetails

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.example.movies.R
import com.example.movies.adapter.TheMovieListAdapter
import com.example.movies.adapter.TheMoviesLoadStateAdapter
import com.example.movies.databinding.FragmentMovieDetailsBinding
import com.example.movies.utils.UiState
import com.example.movies.utils.extensions.collectOnCreated
import com.example.movies.utils.isBrightColor
import com.example.movies.utils.loadImage
import com.example.movies.utils.toPx
import com.google.android.material.transition.MaterialSharedAxis
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class MovieDetailsFragment : Fragment() {

    private val args by navArgs<MovieDetailsFragmentArgs>()
    private val viewModel: MovieDetailsViewModel by viewModel {
        parametersOf(args.movieId, args.type)
    }
    private lateinit var binding: FragmentMovieDetailsBinding

    private val movieListAdapter = TheMovieListAdapter { _, uiState ->
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        findNavController().navigate(
            MovieDetailsFragmentDirections.actionToSimilar(
                uiState.id,
                args.type
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            ViewCompat.setOnApplyWindowInsetsListener(root) { _, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                toolbar.updateLayoutParams<MarginLayoutParams> {
                    topMargin = insets.top
                    leftMargin = insets.left
                    rightMargin = insets.right
                }
                collapsedToolbar.updateLayoutParams {
                    height = (insets.top + 396.toPx(requireContext())).toInt()
                }
                windowInsets
            }

            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            refresher.setOnRefreshListener { viewModel.refresh() }

            viewModel.detailsUiState.collectOnCreated(viewLifecycleOwner) { uiState ->
                refresher.isRefreshing = uiState is UiState.Loading
                when (uiState) {
                    is UiState.Error -> {

                    }

                    UiState.Loading -> {}

                    is UiState.Success -> updateUi(uiState.data)
                }
            }
        }
    }

    private fun FragmentMovieDetailsBinding.updateUi(details: DetailsUiState) {
        title.text = details.title
        posterPath.loadImage(
            details.posterPath,
            error = R.drawable.movie_placeholder,
            allowHardware = false
        )
        backdrop.loadImage(details.backdropPath, allowHardware = false)
        description.text = details.overview
        date.text = details.date
        rating.rating = (details.voteAverage / 2f)

        similarMovies.adapter = movieListAdapter.withLoadStateHeaderAndFooter(
            TheMoviesLoadStateAdapter(movieListAdapter::retry),
            TheMoviesLoadStateAdapter(movieListAdapter::retry)
        )

        viewModel.similarMoviesFlow.collectOnCreated(
            viewLifecycleOwner,
            movieListAdapter::submitData
        )
        viewModel.videosUiState.collectOnCreated(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Error -> {
                    Timber.e(uiState.exception)
                }

                UiState.Loading -> {}

                is UiState.Success -> if (uiState.data.isNotEmpty()) {
                    lifecycle.addObserver(binding.trailerPlayer)
                    binding.trailerPlayer.addYouTubePlayerListener(object :
                        AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.cueVideo(uiState.data.first().key, 0f)
                        }
                    })
                }
            }
        }
    }

}