package com.example.movies.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.movies.data.model.Movie

@Dao
interface MoviesDao {

    @Insert
    suspend fun insertMovies(movies: List<Movie>)

    @Query("SELECT * FROM movies")
    fun paginateMovies(): PagingSource<Int, Movie>

    @Query("DELETE FROM movies WHERE 1;")
    suspend fun clearCache()
}