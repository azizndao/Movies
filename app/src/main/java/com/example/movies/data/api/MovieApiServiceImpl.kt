package com.example.movies.data.api

import com.example.movies.data.model.MovieDetails
import com.example.movies.data.model.MovieLists
import com.example.movies.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class MovieApiServiceImpl(
    private val httpClient: HttpClient
) : MovieApiService {

    override suspend fun getMoviesList(
        page: Int,
        sortKey: String,
        language: String,
        ascendant: Boolean
    ): MovieLists {
        val response = httpClient.get("https://api.themoviedb.org/3/discover/movie") {
            parameter("api_key", Constants.MOVIES_API_KEY)
            parameter("language", language)
            parameter("with_watch_monetization_types", "flatrate")
            parameter("sort_by", "${sortKey}.${if (ascendant) "asc" else "desc"}")
            parameter("page", page)
        }

        return response.body()
    }

    override suspend fun searchMovie(query: String, page: Int, language: String): MovieLists {
        val response = httpClient.get("https://api.themoviedb.org/3/search/movie") {
            parameter("api_key", Constants.MOVIES_API_KEY)
            parameter("language", language)
            parameter("query", query)
            parameter("page", page)
        }

        return response.body()
    }

    override suspend fun getMovieDetails(id: Long, language: String): MovieDetails {
        val response = httpClient.get("https://api.themoviedb.org/3/movie/$id") {
            parameter("api_key", Constants.MOVIES_API_KEY)
            parameter("language", language)
        }

        return response.body()
    }
}