package com.example.movies.data.datasources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movies.data.api.MovieApiService
import com.example.movies.data.model.Movie
import com.example.movies.utils.UserPreferences
import kotlinx.coroutines.flow.first
import timber.log.Timber

class MoviesPagingSource(
    private val preferences: UserPreferences,
    private val api: MovieApiService,
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val nextPageNumber = params.key ?: 1

            val (sortKey, ascendant) = preferences.sortFlow().first()

            val response =
                api.getMoviesList(page = nextPageNumber, sortKey = sortKey, ascendant = ascendant)
            LoadResult.Page(
                data = response.movies,
                prevKey = null,
                nextKey = if (nextPageNumber < response.totalPages) nextPageNumber + 1 else null
            )
        } catch (e: Exception) {
            Timber.e(e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}