package com.example.movies.ui.tvshows

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.movies.R
import com.example.movies.adapter.toUiState
import com.example.movies.data.api.TVApiService
import com.example.movies.data.datasources.TheMoviesPagingSource
import com.example.movies.data.model.TV
import com.example.movies.utils.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TvShowViewModel(
    private val apiService: TVApiService,
    private val preferences: UserPreferences,
    app: Application
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
        }.flow.map { it.map(TV::toUiState) }.cachedIn(viewModelScope),

        R.string.aring_today to Pager(pagingConfig) {
            TheMoviesPagingSource {
                apiService.getAiringToday(
                    it,
                    preferences.getTranslation().first()
                )
            }
        }.flow.map { it.map(TV::toUiState) }.cachedIn(viewModelScope),

        R.string.on_tv to Pager(pagingConfig) {
            TheMoviesPagingSource { apiService.getOnTv(it, preferences.getTranslation().first()) }
        }.flow.map { it.map(TV::toUiState) }.cachedIn(viewModelScope),

        R.string.top_rated to Pager(pagingConfig) {
            TheMoviesPagingSource {
                apiService.getTopRated(
                    it,
                    preferences.getTranslation().first()
                )
            }
        }.flow.map { it.map(TV::toUiState) }.cachedIn(viewModelScope),
    )


}