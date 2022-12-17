package com.example.movies.data.model


import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*


@Keep
@Parcelize
@Serializable
data class Movie(
    val id: Long,

    val adult: Boolean,

    @SerialName("backdrop_path")
    val backdropPath: String? = null,

    @SerialName("genre_ids")
    val genreIDS: List<Long>,

    @SerialName("original_language")
    val originalLanguage: String,

    @SerialName("original_title")
    val originalTitle: String,

    val overview: String,
    val popularity: Float,

    @SerialName("poster_path")
    val posterPath: String? = null,

    @SerialName("release_date")
    val releaseDate: String? = null,

    val title: String,
    val video: Boolean,

    @SerialName("vote_average")
    val voteAverage: Float,

    @SerialName("vote_count")
    val voteCount: Long
) : Parcelable
