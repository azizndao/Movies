package com.example.movies.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.movies.data.model.Movie
import com.example.movies.data.model.TV
import com.example.movies.ui.moviedetails.DataType

data class CardUiState(
    val id: Long,
    val title: String,
    val posterPath: String?,
    val date: String?,
    val voteCount: Long,
    val voteAverage: Float,
    val type: DataType
)

fun TV.toUiState(): CardUiState = CardUiState(
    id,
    name,
    posterPath,
    firstAirDate,
    voteCount = voteCount,
    voteAverage = voteAverage,
    type = DataType.TV_SHOW,
)

fun Movie.toUiState(): CardUiState = CardUiState(
    id, title, posterPath, releaseDate,
    voteCount = voteCount,
    voteAverage = voteAverage,
    type = DataType.TV_SHOW,
)

object CardItemComparator : DiffUtil.ItemCallback<CardUiState>() {
    override fun areItemsTheSame(oldItem: CardUiState, newItem: CardUiState): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CardUiState, newItem: CardUiState): Boolean {
        return oldItem == newItem
    }
}