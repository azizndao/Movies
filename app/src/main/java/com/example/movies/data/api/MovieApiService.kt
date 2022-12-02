package com.example.movies.data.api

import com.example.movies.data.model.MovieDetails
import com.example.movies.data.model.MovieLists

interface MovieApiService {

    suspend fun getMoviesList(
        page: Int,
        sortKey: String,
        language: String = "en-US",
        ascendant: Boolean
    ): MovieLists

    suspend fun searchMovie(query: String, page: Int, language: String = "en-US"): MovieLists

    suspend fun getMovieDetails(id: Long, language: String = "en-US"): MovieDetails
}
