package com.example.movies.ui.translation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.utils.Constants
import com.example.movies.utils.UiState
import com.example.movies.utils.UserPreferences
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale

class TranslationsViewModel(
    private val preferences: UserPreferences,
    private val httpClient: HttpClient,
    app: Application
) : AndroidViewModel(app) {

    val uiStateFlow = MutableStateFlow<UiState<List<TranslationUiState>>>(UiState.Loading)

    private var job: Job? = null

    init {
        refresh()
    }

    private fun refresh() {
        job?.cancel()
        job = viewModelScope.launch {
            preferences.getTranslation().collect { selectedCode ->
                uiStateFlow.value = try {
                    val response = httpClient
                        .get("https://api.themoviedb.org/3/configuration/primary_translations") {
                            parameter("api_key", Constants.MOVIES_API_KEY)
                        }
                    val items = response.body<List<String>>().map { languageCode ->
                        val (language, country) = languageCode.split('-')
                        val locale = Locale(language, country)
                        TranslationUiState(
                            code = languageCode,
                            name = locale.displayName,
                            selected = languageCode == selectedCode
                        )
                    }.sortedBy { it.name }.sortedBy { it.selected }
                    UiState.Success(items)
                } catch (e: Exception) {
                    Timber.e(e)
                    UiState.Error(e)
                }
            }
        }
    }

    suspend fun setTranslation(uiState: TranslationUiState) {
        preferences.setTranslation(uiState.code)
    }
}