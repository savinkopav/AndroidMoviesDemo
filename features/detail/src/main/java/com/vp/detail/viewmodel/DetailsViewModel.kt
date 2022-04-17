package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vp.detail.QueryProvider
import com.vp.detail.data.local.db.model.Movie
import com.vp.detail.domain.MovieRepository
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(
    private val detailService: DetailService,
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    private val insertingDatabaseState: MutableLiveData<InsertingDatabaseState> = MutableLiveData()

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun insertingState(): LiveData<InsertingDatabaseState> = insertingDatabaseState

    fun clearInsertingState() {
        insertingDatabaseState.value = null
    }

    fun fetchDetails(queryProvider: QueryProvider) {
        loadingState.value = LoadingState.IN_PROGRESS
        detailService.getMovie(queryProvider.getMovieId()).enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
            override fun onResponse(call: Call<MovieDetail>?, response: Response<MovieDetail>?) {
                details.postValue(response?.body())

                response?.body()?.title?.let {
                    title.postValue(it)
                }

                loadingState.value = LoadingState.LOADED
            }

            override fun onFailure(call: Call<MovieDetail>?, t: Throwable?) {
                details.postValue(null)
                loadingState.value = LoadingState.ERROR
            }
        })
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }

    enum class InsertingDatabaseState {
        SUCCESS,
        ERROR,
        IN_PROGRESS
    }

    fun saveMovie(movieDetail: MovieDetail, movieId: String) {
        insertingDatabaseState.value = InsertingDatabaseState.IN_PROGRESS
        var insertedItem = 0L
        viewModelScope.launch {
            insertedItem = movieRepository.insertMovie(
                Movie(
                    movieDetail.title,
                    movieDetail.year,
                    movieDetail.runtime,
                    movieDetail.director,
                    movieDetail.plot,
                    movieDetail.poster,
                    movieId
                )
            )
            if (insertedItem < 0)  {
                insertingDatabaseState.value = InsertingDatabaseState.ERROR
            } else {
                insertingDatabaseState.value = InsertingDatabaseState.SUCCESS
            }
        }
    }

}