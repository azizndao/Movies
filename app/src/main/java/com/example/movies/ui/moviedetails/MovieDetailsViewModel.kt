package com.example.movies.ui.moviedetails

import androidx.lifecycle.ViewModel
import com.example.movies.data.model.Movie
import com.example.movies.data.api.MovieApiService
import kotlinx.coroutines.flow.flow

class MovieDetailsViewModel(
    val movie: Movie,
    private val apiRepository: MovieApiService,
) : ViewModel() {

    val uiState = flow { emit(apiRepository.getMovieDetails(movie.id)) }
}