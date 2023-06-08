package com.example.movies.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.movies.data.dao.GenreDao
import com.example.movies.data.dao.MoviesDao
import com.example.movies.data.model.Genre
import com.example.movies.data.model.Movie

@Database(version = 1, entities = [Movie::class, Genre::class], exportSchema = true)
@TypeConverters(LongListConvertor::class)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao

    abstract fun genresDao(): GenreDao
}

class LongListConvertor {

    @TypeConverter
    fun encode(value: List<Long>): String = value.joinToString(",")

    @TypeConverter
    fun decode(value: String): List<Long> = value.split(",").map(String::toLong)
}