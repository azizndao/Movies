package com.example.movies.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.bumptech.glide.load.HttpException
import com.example.movies.data.api.MovieApiService
import com.example.movies.data.model.Movie
import com.example.movies.utils.UserPreferences
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val preferences: UserPreferences,
    private val database: MoviesDatabase,
    private val networkService: MovieApiService
) : RemoteMediator<Int, Movie>() {
    val userDao = database.moviesDao()
//    override suspend fun initialize(): InitializeAction {
//        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
//        return if (System.currentTimeMillis() - database.lastUpdated() <= cacheTimeout) {
//            // Cached data is up-to-date, so there is no need to re-fetch
//            // from the network.
//            InitializeAction.SKIP_INITIAL_REFRESH
//        } else {
//            // Need to refresh cached data from network; returning
//            // LAUNCH_INITIAL_REFRESH here will also block RemoteMediator's
//            // APPEND and PREPEND from running until REFRESH succeeds.
//            InitializeAction.LAUNCH_INITIAL_REFRESH
//        }
//    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {
        return try {
            // The network load method takes an optional after=<user.id>
            // parameter. For every page after the first, pass the last user
            // ID to let it continue from where it left off. For REFRESH,
            // pass null to load the first page.
            Timber.e(state.anchorPosition)

            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                // In this example, you never need to prepend, since REFRESH
                // will always load the first page in the list. Immediately
                // return, reporting end of pagination.
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val lastItem = state.pages.last().nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)

                    // You must explicitly check if the last item is null when
                    // appending, since passing null to networkService is only
                    // valid for initial load. If lastItem is null it means no
                    // items were loaded after the initial REFRESH and there are
                    // no more items to load.

                    lastItem
                }
            }

            // Suspending network load via Retrofit. This doesn't need to be
            // wrapped in a withContext(Dispatcher.IO) { ... } block since
            // Retrofit's Coroutine CallAdapter dispatches on a worker
            // thread.

            val (sortKey, ascendant) = preferences.sortFlow().first()
            val response = networkService.getMoviesList(
                page = loadKey,
                sortKey = sortKey,
                ascendant = ascendant
            )

//            if (loadType == LoadType.REFRESH) {
//                userDao.clearCache(query)
//            }

            // Insert new users into database, which invalidates the
            // current PagingData, allowing Paging to present the updates
            // in the DB.
            userDao.insertMovies(response.movies)

            MediatorResult.Success(endOfPaginationReached = response.page == response.totalPages)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}