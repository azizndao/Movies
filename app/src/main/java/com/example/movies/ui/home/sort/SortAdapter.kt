package com.example.movies.ui.home.sort

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.movies.R
import com.example.movies.databinding.SortItemBinding

class SortAdapter(
    private val onClick: (MovieSortUiState) -> Unit
) : ListAdapter<MovieSortUiState, SortAdapter.MovieSortUiStateViewHolder>(MovieSortUiStateComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MovieSortUiStateViewHolder(parent, onClick)

    override fun onBindViewHolder(holder: MovieSortUiStateViewHolder, position: Int) =
        holder.bindTo(getItem(position))

    object MovieSortUiStateComparator : DiffUtil.ItemCallback<MovieSortUiState>() {
        override fun areItemsTheSame(
            oldItem: MovieSortUiState,
            newItem: MovieSortUiState
        ): Boolean =
            oldItem.label == newItem.label

        override fun areContentsTheSame(oldItem: MovieSortUiState, newItem: MovieSortUiState) =
            oldItem == newItem

    }

    class MovieSortUiStateViewHolder(
        parent: ViewGroup,
        private val onClick: (MovieSortUiState) -> Unit
    ) : ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.sort_item, parent, false)
    ) {
        private val binding = SortItemBinding.bind(itemView)

        fun bindTo(sort: MovieSortUiState) {
            with(binding) {
                root.setOnClickListener { onClick(sort) }
                label.text = sort.label

                sortOrder.isInvisible = !sort.selected

                root.background = if (sort.selected) {
                    ResourcesCompat.getDrawable(
                        root.resources,
                        R.drawable.bg_sort_item,
                        root.context.theme
                    )
                } else {
                    ColorDrawable(0x000000)
                }

                if (!sort.selected) return

                sortOrder.animate()
                    .rotation(if (sort.ascendant) 180f else 0f)
                    .start()
            }
        }
    }
}