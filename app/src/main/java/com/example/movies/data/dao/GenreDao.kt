package com.example.movies.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.data.model.Genre
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(genres: List<Genre>)

    @Query("SELECT * FROM genres")
    suspend fun getAll(): List<Genre>

    @Query("SELECT * FROM genres")
    fun observeAll(): Flow<List<Genre>>

    @Query("DELETE FROM genres;")
    suspend fun clearAll()
}