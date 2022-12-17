package com.example.movies.ui.translation

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.movies.R
import com.example.movies.databinding.TranslationItemBinding

class TranslationsAdapter(
    private val onClick: (TranslationUiState) -> Unit
) : ListAdapter<TranslationUiState, TranslationsAdapter.MovieSortUiStateViewHolder>(
    MovieSortUiStateComparator
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MovieSortUiStateViewHolder(parent, onClick)

    override fun onBindViewHolder(holder: MovieSortUiStateViewHolder, position: Int) =
        holder.bindTo(getItem(position))

    object MovieSortUiStateComparator : DiffUtil.ItemCallback<TranslationUiState>() {
        override fun areItemsTheSame(
            oldItem: TranslationUiState,
            newItem: TranslationUiState
        ): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: TranslationUiState, newItem: TranslationUiState) =
            oldItem == newItem

    }

    class MovieSortUiStateViewHolder(
        parent: ViewGroup,
        private val onClick: (TranslationUiState) -> Unit
    ) : ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.translation_item, parent, false)
    ) {
        private val binding = TranslationItemBinding.bind(itemView)

        fun bindTo(sort: TranslationUiState) {
            with(binding.label) {
                setOnClickListener { onClick(sort) }
                text = sort.name

                background = if (sort.selected) {
                    ResourcesCompat.getDrawable(resources, R.drawable.bg_sort_item, context.theme)
                } else {
                    ColorDrawable(0x000000)
                }
            }
        }
    }
}