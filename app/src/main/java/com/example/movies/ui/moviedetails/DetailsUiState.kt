package com.example.movies.ui.moviedetails

import com.example.movies.data.model.MovieDetails
import com.example.movies.data.model.TVDetails

data class DetailsUiState(
    val id: Long,
    val title: String,
    val overview: String?,
    val backdropPath: String?,
    val posterPath: String?,
    val voteCount: Long,
    val voteAverage: Float,
    val date: String?,
)


fun MovieDetails.toUiState() = DetailsUiState(
    id = id,
    title = title,
    overview = overview,
    backdropPath = backdropPath,
    posterPath = posterPath,
    voteCount = voteCount,
    voteAverage = voteAverage,
    date = releaseDate
)

fun TVDetails.toUiState() = DetailsUiState(
    id = id,
    title = name,
    overview = overview,
    backdropPath = backdropPath,
    posterPath = posterPath,
    voteCount = voteCount,
    voteAverage = voteAverage,
    date = firstAirDate
)