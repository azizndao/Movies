package com.example.movies.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GenresListResponse(
    val genres: List<Genre>
)
