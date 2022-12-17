package com.example.movies.ui

import com.example.movies.ui.home.HomeViewModel
import com.example.movies.ui.translation.TranslationsViewModel
import com.example.movies.ui.moviedetails.MovieDetailsViewModel
import com.example.movies.ui.movies.MoviesViewModel
import com.example.movies.ui.search.SearchViewModel
import com.example.movies.ui.tvdetails.TvSDetailsViewModel
import com.example.movies.ui.tvshows.TvShowViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val UiModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::MoviesViewModel)
    viewModelOf(::TvShowViewModel)
    viewModelOf(::TvSDetailsViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::MovieDetailsViewModel)
    viewModelOf(::TranslationsViewModel)
}