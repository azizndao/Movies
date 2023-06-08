package com.example.movies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.data.model.Movie
import com.example.movies.databinding.MovieItemBinding
import com.example.movies.utils.loadImage
import com.example.movies.utils.yearFromDate

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

        private val binding = MovieItemBinding.bind(itemView)

        fun bindTo(movie: Movie?) {
            with(binding) {
                if (movie != null) {
                    cover.setOnClickListener { onClick(it, movie) }

                    cover.loadImage(movie.posterPath ?: movie.backdropPath)

                    title.text = movie.title
                    description.yearFromDate(movie.releaseDate)
                }
            }
        }
    }

    object MovieComparator : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }
}