package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vp.detail.data.local.db.model.Movie
import com.vp.detail.domain.MovieRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {

    private val movies: MutableLiveData<List<Movie>> = MutableLiveData()

    fun movies(): LiveData<List<Movie>> = movies

    fun getMovies() {
        viewModelScope.launch {
            movies.postValue(movieRepository.getMovies())
        }
    }
}