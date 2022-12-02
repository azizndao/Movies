package com.example.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.databinding.MovieLoadStateItemBinding
import timber.log.Timber

class MoviesLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<MoviesLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState
    ) = LoadStateViewHolder(parent, retry)

    override fun onBindViewHolder(
        holder: LoadStateViewHolder, loadState: LoadState
    ) = holder.bind(loadState)

    class LoadStateViewHolder(
        parent: ViewGroup,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.movie_load_state_item, parent, false)
    ) {

        private val binding = MovieLoadStateItemBinding.bind(itemView).apply {
            setOnRetry { retry() }
        }

        fun bind(loadState: LoadState) {
            with(binding) {
                Timber.e(loadState.toString())
                progressIndicator.isVisible = loadState is LoadState.Loading
                btnRetry.isVisible = loadState is LoadState.Error

                errorMessage.isVisible = loadState is LoadState.Error
                if (loadState is LoadState.Error) {
                    errorMessage.text = loadState.error.localizedMessage
                }
            }
        }
    }
}