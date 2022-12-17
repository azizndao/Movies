package com.example.movies.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.movies.adapter.toUiState
import com.example.movies.data.api.MovieApiService
import com.example.movies.data.datasources.MovieSearchPagingSource
import com.example.movies.data.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class SearchViewModel(
    private val apiRepository: MovieApiService,
    app: Application
) : AndroidViewModel(app) {


    private val searchQuery = MutableStateFlow("")

    val dataFlow = searchQuery.flatMapLatest { query ->
        Pager(PagingConfig(pageSize = 20)) { MovieSearchPagingSource(query, apiRepository) }
            .flow.map { it.map(Movie::toUiState) }.cachedIn(viewModelScope)
    }

    fun updateQuery(value: String) {
        searchQuery.value = value
    }
}