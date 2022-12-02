package com.example.movies.ui

import com.example.movies.ui.home.HomeViewModel
import com.example.movies.ui.home.sort.SortViewModel
import com.example.movies.ui.moviedetails.MovieDetailsViewModel
import com.example.movies.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val UiModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::MovieDetailsViewModel)
    viewModelOf(::SortViewModel)
}