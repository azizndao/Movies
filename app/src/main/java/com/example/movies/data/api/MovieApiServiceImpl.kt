package com.example.movies.data.api

import com.example.movies.data.model.Genre
import com.example.movies.data.model.GenresListResponse
import com.example.movies.data.model.Movie
import com.example.movies.data.model.MovieDetails
import com.example.movies.data.model.TheMovieResponse
import com.example.movies.data.model.Video
import com.example.movies.data.model.VideoResponse
import com.example.movies.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class MovieApiServiceImpl(
    private val httpClient: HttpClient
) : MovieApiService {

    private val baseUrl = "https://api.themoviedb.org/3"

    override suspend fun getMoviesList(
        page: Int,
        sortKey: String,
        language: String,
        ascendant: Boolean
    ): TheMovieResponse<Movie> {
        return httpClient.get("$baseUrl/discover/movie") {
            parameter("api_key", Constants.MOVIES_API_KEY)
            parameter("language", language)
            parameter("with_watch_monetization_types", "flatrate")
            parameter("sort_by", "${sortKey}.${if (ascendant) "asc" else "desc"}")
            parameter("page", page)
        }.body()
    }

    override suspend fun getPopular(page: Int, language: String): TheMovieResponse<Movie> {
        return httpClient.get("$baseUrl/movie/popular") {
            parameter("api_key", Constants.MOVIES_API_KEY)
            parameter("language", language)
            parameter("page", page)
        }.body()
    }

    override suspend fun getTopRated(page: Int, language: String): TheMovieResponse<Movie> {
        return httpClient.get("$baseUrl/movie/top_rated") {
            parameter("api_key", Constants.MOVIES_API_KEY)
            parameter("language", language)
            parameter("page", page)
        }.body()
    }

    override suspend fun getUpcoming(page: Int, language: String): TheMovieResponse<Movie> {
        return httpClient.get("$baseUrl/movie/upcoming") {
            parameter("api_key", Constants.MOVIES_API_KEY)
            parameter("language", language)
            parameter("page", page)
        }.body()
    }

    override suspend fun getNowPlaying(page: Int, language: String): TheMovieResponse<Movie> {
        return httpClient.get("$baseUrl/movie/now_playing") {
            parameter("api_key", Constants.MOVIES_API_KEY)
            parameter("language", language)
            parameter("page", page)
        }.body()
    }

    override suspend fun getLatest(page: Int, language: String): TheMovieResponse<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun searchMovie(
        query: String,
        page: Int,
        language: String
    ): TheMovieResponse<Movie> {
        val response = httpClient.get("$baseUrl/search/movie") {
            parameter("api_key", Constants.MOVIES_API_KEY)
            parameter("language", language)
            parameter("query", query)
            parameter("page", page)
        }
        return response.body()
    }

    override suspend fun getMovieDetails(id: Long, language: String): MovieDetails {
        return httpClient.get("$baseUrl/movie/$id") {
            parameter("api_key", Constants.MOVIES_API_KEY)
            parameter("language", language)
        }.body()
    }

    override suspend fun getSimilarMovies(id: Long, language: String): TheMovieResponse<Movie> {
        return httpClient.get("$baseUrl/movie/$id/similar") {
            parameter("api_key", Constants.MOVIES_API_KEY)
            parameter("language", language)
        }.body()
    }

    override suspend fun getAllGenres(language: String): List<Genre> {
        return httpClient.get("$baseUrl/genre/movie/list") {
            parameter("api_key", Constants.MOVIES_API_KEY)
            parameter("language", language)
        }.body<GenresListResponse>().genres
    }

    override suspend fun getMovieVideos(movieId: Long, language: String): List<Video> {
        return httpClient.get("$baseUrl/movie/$movieId/videos") {
            parameter("api_key", Constants.MOVIES_API_KEY)
            parameter("language", language)
        }.body<VideoResponse>().results
    }
}