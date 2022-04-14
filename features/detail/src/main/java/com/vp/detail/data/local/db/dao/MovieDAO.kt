package com.vp.detail.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vp.detail.data.local.db.model.Movie

@Dao
interface MovieDAO {

    @Query("SELECT * FROM movies ORDER BY id DESC")
    suspend fun getMovies() : List<Movie>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movie: Movie) : Long

}