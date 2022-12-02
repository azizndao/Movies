package com.example.movies.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.movies.data.api.MovieApiService
import com.example.movies.data.datasources.MoviesPagingSource
import com.example.movies.utils.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    apiService: MovieApiService,
    private val preferences: UserPreferences
) : ViewModel() {

    val dataFlow = Pager(config = PagingConfig(pageSize = 20)) {
        MoviesPagingSource(api = apiService, preferences = preferences)
    }.flow.cachedIn(viewModelScope)

    val sortFlow = preferences.sortFlow().stateIn(viewModelScope, SharingStarted.Eagerly, null)
}