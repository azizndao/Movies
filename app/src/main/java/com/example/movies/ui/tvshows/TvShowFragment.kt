package com.example.movies.ui.tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.movies.R
import com.example.movies.databinding.FragmentTabsBinding
import com.example.movies.ui.movies.MovieCategoryFragment
import com.google.android.material.tabs.TabLayoutMediator

class TvShowFragment : Fragment() {

    private lateinit var binding: FragmentTabsBinding


    private lateinit var viewModel: TvShowViewModel
    val tabs = listOf(R.string.popular, R.string.aring_today, R.string.on_tv, R.string.top_rated)

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
            toolbar.title = getString(R.string.tv)
            TabLayoutMediator(tabLayout, pager) { tab, index ->
                tab.text = getString(tabs[index])
            }.attach()
        }
    }

    inner class PageAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int = tabs.size
        override fun createFragment(position: Int) = TVCategoryFragment().apply {
            arguments = bundleOf(MovieCategoryFragment.ID to tabs[position])
        }
    }
}