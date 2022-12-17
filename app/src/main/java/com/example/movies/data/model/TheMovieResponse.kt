package com.example.movies.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TheMovieResponse<T>(
    val page: Long,

    @SerialName("results")
    val movies: List<T>,

    @SerialName("total_pages")
    val totalPages: Long,

    @SerialName("total_results")
    val totalResults: Long,

    val dates: Dates? = null
)

@Serializable
data class Dates(
    val maximum: String,
    val minimum: String
)