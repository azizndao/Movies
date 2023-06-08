package com.example.movies.data

import androidx.room.Room
import com.example.movies.data.api.MovieApiService
import com.example.movies.data.api.MovieApiServiceImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val RepoModule = module {
    factoryOf(::MovieApiServiceImpl) bind MovieApiService::class

    factoryOf(::MovieRemoteMediator)

    single {
        Room.databaseBuilder(get(), MoviesDatabase::class.java, "movies.db")
            .build()
    }

    factory { get<MoviesDatabase>().moviesDao() }
}