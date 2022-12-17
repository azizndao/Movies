package com.example.movies.ui.tvdetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movies.R

class TvSDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = TvSDetailsFragment()
    }

    private lateinit var viewModel: TvSDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tv_s_details, container, false)
    }

}