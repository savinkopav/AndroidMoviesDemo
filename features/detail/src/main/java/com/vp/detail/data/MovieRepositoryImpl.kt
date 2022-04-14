package com.vp.detail.data

import com.vp.detail.data.local.db.MovieDatabase
import com.vp.detail.data.local.db.model.Movie
import com.vp.detail.domain.MovieRepository

class MovieRepositoryImpl(private val db: MovieDatabase) : MovieRepository {

    override suspend fun insertMovie(movie: Movie) : Long {
        return db.movieDao().insertMovie(movie)
    }

    override suspend fun getMovies(): List<Movie> {
        return db.movieDao().getMovies()
    }
}