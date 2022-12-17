package com.example.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.databinding.MovieLoadStateItemBinding

class TheMoviesLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<TheMoviesLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState
    ) = LoadStateViewHolder(parent, retry)

    override fun onBindViewHolder(
        holder: LoadStateViewHolder, loadState: LoadState
    ) = holder.bind(loadState)

    class LoadStateViewHolder(
        parent: ViewGroup,
        private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.movie_load_state_item, parent, false)
    ) {

        private val binding = MovieLoadStateItemBinding.bind(itemView)

        fun bind(loadState: LoadState) {
            with(binding) {
                progressIndicator.isVisible = loadState is LoadState.Loading
                btnRetry.isVisible = loadState is LoadState.Error
                btnRetry.setOnClickListener { retry() }

                errorMessage.isVisible = loadState is LoadState.Error
                if (loadState is LoadState.Error) {
                    errorMessage.text = loadState.error.localizedMessage
                }
            }
        }
    }
}