package com.example.movies.data.model

import kotlinx.serialization.Serializable

@Serializable
data class VideoResponse(
    val id: Long,
    val results: List<Video>
)
