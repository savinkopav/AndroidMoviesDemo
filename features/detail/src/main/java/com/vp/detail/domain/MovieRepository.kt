package com.vp.detail.domain

import com.vp.detail.data.local.db.model.Movie

interface MovieRepository {

    suspend fun insertMovie(movie: Movie) : Long

    suspend fun getMovies() : List<Movie>
}