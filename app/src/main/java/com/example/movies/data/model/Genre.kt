package com.example.movies.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "genres")
@Serializable
data class Genre(
    @PrimaryKey val id: Long,
    val name: String
)