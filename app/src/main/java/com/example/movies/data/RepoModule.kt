package com.example.movies.data

import com.example.movies.data.api.MovieApiService
import com.example.movies.data.api.MovieApiServiceImpl
import com.example.movies.data.api.TVApiService
import com.example.movies.data.api.TVApiServiceImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val RepoModule = module {
    factoryOf(::MovieApiServiceImpl) bind MovieApiService::class
    factoryOf(::TVApiServiceImpl) bind TVApiService::class
}