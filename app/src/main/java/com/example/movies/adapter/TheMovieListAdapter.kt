package com.example.movies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.movies.R
import com.example.movies.databinding.ListCardItemBinding
import com.example.movies.utils.ImageHelper
import com.example.movies.utils.yearFromDate

class TheMovieListAdapter(
    private val onCardItemUiStateClick: (View, CardUiState) -> Unit
) : PagingDataAdapter<CardUiState, TheMovieListAdapter.CardItemUiStateViewHolder>(
    CardItemComparator
) {

    override fun onBindViewHolder(holder: CardItemUiStateViewHolder, position: Int) =
        holder.bindTo(getItem(position)!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CardItemUiStateViewHolder(parent, onCardItemUiStateClick)

    class CardItemUiStateViewHolder(
        parent: ViewGroup,
        private val onClick: (View, CardUiState) -> Unit
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_card_item, parent, false)
    ) {

        private val binding = ListCardItemBinding.bind(itemView)

        fun bindTo(uiState: CardUiState) {
            with(binding) {
                posterImage.setOnClickListener { onClick(it, uiState) }
                posterImage.load(uiState.posterPath?.let { ImageHelper.getImage(400, it) }) {
                    placeholder(R.drawable.movie_placeholder)
                    error(R.drawable.movie_placeholder)
                }
                title.text = uiState.title
                rating.rating = uiState.voteAverage / 2f
                date.yearFromDate(uiState.date)
            }
        }
    }
}