package com.example.movies.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.movies.adapter.toUiState
import com.example.movies.data.api.MovieApiService
import com.example.movies.data.api.TVApiService
import com.example.movies.data.datasources.TheMoviesPagingSource
import com.example.movies.data.model.Movie
import com.example.movies.data.model.TV
import com.example.movies.utils.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class HomeViewModel(
    movieApiService: MovieApiService,
    tvApiService: TVApiService,
    preferences: UserPreferences
) : ViewModel() {

    val moviesDataFlow = Pager(config = PagingConfig(pageSize = 20)) {
        TheMoviesPagingSource { page ->
            movieApiService.getPopular(
                page,
                preferences.getTranslation().first()
            )
        }
    }.flow.map { it.map(Movie::toUiState) }.cachedIn(viewModelScope)

    val tvDataFlow = Pager(config = PagingConfig(pageSize = 20)) {
        TheMoviesPagingSource { page ->
            tvApiService.getPopular(
                page,
                preferences.getTranslation().first()
            )
        }
    }.flow.map { it.map(TV::toUiState) }.cachedIn(viewModelScope)
}