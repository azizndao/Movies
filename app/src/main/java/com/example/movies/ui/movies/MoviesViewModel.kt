package com.example.movies.ui.movies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.movies.R
import com.example.movies.adapter.toUiState
import com.example.movies.data.api.MovieApiService
import com.example.movies.data.datasources.TheMoviesPagingSource
import com.example.movies.data.model.Movie
import com.example.movies.utils.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class MoviesViewModel(
    private val apiService: MovieApiService,
    private val preferences: UserPreferences,
    app: Application,
) : AndroidViewModel(app) {
    private val pagingConfig = PagingConfig(pageSize = 20, enablePlaceholders = true)

    var tabViews = mapOf(
        R.string.popular to Pager(pagingConfig) {
            TheMoviesPagingSource {
                apiService.getPopular(
                    it,
                    preferences.getTranslation().first()
                )
            }
        }.flow.map { it.map(Movie::toUiState) }.cachedIn(viewModelScope),

        R.string.now_playing to Pager(pagingConfig) {
            TheMoviesPagingSource {
                apiService.getNowPlaying(
                    it,
                    preferences.getTranslation().first()
                )
            }
        }.flow.map { it.map(Movie::toUiState) }.cachedIn(viewModelScope),

        R.string.upcoming to Pager(pagingConfig) {
            TheMoviesPagingSource {
                apiService.getUpcoming(
                    it,
                    preferences.getTranslation().first()
                )
            }
        }.flow.map { it.map(Movie::toUiState) }.cachedIn(viewModelScope),

        R.string.top_rated to Pager(pagingConfig) {
            TheMoviesPagingSource {
                apiService.getTopRated(
                    it,
                    preferences.getTranslation().first()
                )
            }
        }.flow.map { it.map(Movie::toUiState) }.cachedIn(viewModelScope),
    )

}