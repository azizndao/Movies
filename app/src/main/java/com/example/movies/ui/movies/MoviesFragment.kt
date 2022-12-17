package com.example.movies.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.movies.R
import com.example.movies.databinding.FragmentTabsBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesFragment : Fragment() {

    private val viewModel: MoviesViewModel by viewModel()
    private lateinit var binding: FragmentTabsBinding
    val tabs = listOf(R.string.popular, R.string.now_playing, R.string.upcoming, R.string.top_rated)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            pager.adapter = PageAdapter()
            TabLayoutMediator(tabLayout, pager) { tab, index -> tab.text = getString(tabs[index]) }
                .attach()
        }
    }

    inner class PageAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int = tabs.size
        override fun createFragment(position: Int) = MovieCategoryFragment().apply {
            arguments = bundleOf(MovieCategoryFragment.ID to tabs[position])
        }
    }
}