package com.example.movies.ui.translation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.movies.databinding.FragmentTranslationsBottomSheetBinding
import com.example.movies.utils.UiState
import com.example.movies.utils.extensions.collectOnCreated
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TranslationsBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentTranslationsBottomSheetBinding
    private val viewModel by viewModel<TranslationsViewModel>()
    private val translationsAdapter = TranslationsAdapter { uiState ->
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.setTranslation(uiState)
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTranslationsBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            sortKeys.adapter = translationsAdapter

            viewModel.uiStateFlow.collectOnCreated(viewLifecycleOwner) { uiState ->
                when (uiState) {
                    is UiState.Error -> {}

                    UiState.Loading -> {}

                    is UiState.Success -> translationsAdapter.submitList(uiState.data)
                }
            }
        }
    }
}