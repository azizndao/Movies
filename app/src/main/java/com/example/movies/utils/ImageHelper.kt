package com.example.movies.utils

object ImageHelper {
    private const val BASE_URL = "https://image.tmdb.org/t/p"

    fun getImage(width: Int, path: String): String {
        return "$BASE_URL/w$width$path"
    }

    fun getImage(path: String): String {
        return "$BASE_URL/original$path"
    }
}