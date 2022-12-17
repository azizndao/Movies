package com.example.movies.ui.moviedetails

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
import com.example.movies.data.model.Video
import com.example.movies.utils.UiState
import com.example.movies.utils.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val id: Long,
    private val type: DataType,
    private val movieApiService: MovieApiService,
    private val tvApiService: TVApiService,
    private val preferences: UserPreferences,
) : ViewModel() {

    val detailsUiState = MutableStateFlow<UiState<DetailsUiState>>(UiState.Loading)
    val videosUiState = MutableStateFlow<UiState<List<Video>>>(UiState.Loading)

    val similarMoviesFlow = when (type) {
        DataType.MOVIE -> Pager(PagingConfig(20)) {
            TheMoviesPagingSource {
                movieApiService.getSimilarMovies(
                    id,
                    preferences.getTranslation().first()
                )
            }
        }.flow.map { it.map(Movie::toUiState) }

        DataType.TV_SHOW -> Pager(PagingConfig(20)) {
            TheMoviesPagingSource {
                tvApiService.getSimilarTvs(
                    id,
                    preferences.getTranslation().first()
                )
            }
        }.flow.map { it.map(TV::toUiState) }
    }.cachedIn(viewModelScope)

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            fetchDetails()
            loadVideos()
        }
    }

    private suspend fun fetchDetails() {
        detailsUiState.value = try {
            val uiState = when (type) {
                DataType.MOVIE -> movieApiService.getMovieDetails(
                    id,
                    preferences.getTranslation().first()
                ).toUiState()

                DataType.TV_SHOW -> tvApiService.getTvDetails(
                    id,
                    preferences.getTranslation().first()
                ).toUiState()
            }
            UiState.Success(uiState)
        } catch (e: Exception) {
            UiState.Error(e)
        }
    }

    private suspend fun loadVideos() {
        videosUiState.value = try {
            val videos = when (type) {
                DataType.MOVIE -> movieApiService.getMovieVideos(
                    id,
                    preferences.getTranslation().first()
                )

                DataType.TV_SHOW -> tvApiService.getTvVideos(
                    id,
                    preferences.getTranslation().first()
                )
            }
            UiState.Success(videos)
        } catch (e: Exception) {
            UiState.Error(e)
        }
    }
}