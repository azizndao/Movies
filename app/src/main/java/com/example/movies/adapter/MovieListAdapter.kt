package com.example.movies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.databinding.MovieItemBinding
import com.example.movies.data.model.Movie

class MovieListAdapter(
    private val onMovieClick: (View, Movie) -> Unit
) : PagingDataAdapter<Movie, MovieListAdapter.MovieViewHolder>(MovieComparator) {


    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) =
        holder.bindTo(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MovieViewHolder(parent, onMovieClick)

    class MovieViewHolder(
        parent: ViewGroup,
        private val onClick: (View, Movie) -> Unit
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
    ) {

        private val binding = MovieItemBinding.bind(itemView).apply {
            setOnClick { onClick(it, movie!!) }
        }

        fun bindTo(movie: Movie?) {
            binding.movie = movie
        }
    }

    object MovieComparator : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }
}