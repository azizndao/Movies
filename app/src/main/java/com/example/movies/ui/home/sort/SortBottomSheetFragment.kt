package com.example.movies.ui.home.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.movies.databinding.FragmentSortBottomSheetBinding
import com.example.movies.utils.extensions.collectOnCreated
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SortBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSortBottomSheetBinding
    private val viewModel by viewModel<SortViewModel>()
    private val sortAdapter = SortAdapter { uiState ->
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.setSort(uiState)
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSortBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            sortKeys.adapter = sortAdapter

            viewModel.uiStateFlow.collectOnCreated(viewLifecycleOwner, sortAdapter::submitList)
        }
    }
}