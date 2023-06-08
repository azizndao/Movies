package com.example.movies.ui.moviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.movies.databinding.FragmentMovieDetailsBinding
import com.example.movies.utils.extensions.collectOnCreated
import com.example.movies.utils.loadImage
import com.example.movies.utils.yearFromDate
import com.google.android.material.transition.MaterialSharedAxis
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MovieDetailsFragment : Fragment() {

    private val args by navArgs<MovieDetailsFragmentArgs>()
    private val viewModel by viewModel<MovieDetailsViewModel> { parametersOf(args.movie) }
    private lateinit var binding: FragmentMovieDetailsBinding

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
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            backdrop.loadImage(args.movie.backdropPath)
            posterPath.loadImage(args.movie.posterPath)
            title.text = args.movie.title
            date.yearFromDate(args.movie.releaseDate)
            description.text = args.movie.overview
            rating.rating = (args.movie.voteAverage / 2f).toFloat()
        }

        viewModel.uiState.collectOnCreated(viewLifecycleOwner) { binding.description.text = it.overview }
    }
}