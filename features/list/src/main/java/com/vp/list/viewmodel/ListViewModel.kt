package com.vp.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.list.model.ListItem
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ListViewModel @Inject internal constructor(private val searchService: SearchService) : ViewModel() {

    private val liveData = MutableLiveData<SearchResult>()
    private var currentTitle = ""
    private val aggregatedItems: MutableList<ListItem> = ArrayList()
    private val loadingStatus: MutableLiveData<LoadingStatus> = MutableLiveData()

    fun observeMovies(): LiveData<SearchResult> {
        return liveData
    }

    fun loadingStatus() : LiveData<LoadingStatus> {
        return loadingStatus
    }

    fun searchMoviesByTitle(title: String, page: Int) {
        loadingStatus.value = LoadingStatus.LOADING
        if (page == 1 && title != currentTitle) {
            aggregatedItems.clear()
            currentTitle = title
            liveData.value = SearchResult.inProgress()
        }
        searchService.search(title, page).enqueue(object : Callback<SearchResponse?> {
            override fun onResponse(call: Call<SearchResponse?>, response: Response<SearchResponse?>) {
                loadingStatus.value = LoadingStatus.SUCCESS
                val result: SearchResponse? = response.body()

                result?.let { result ->
                    result.search?.let {
                        aggregatedItems.addAll(it)
                        liveData.value = SearchResult.success(aggregatedItems, result.totalResults)
                    }
                }
            }

            override fun onFailure(call: Call<SearchResponse?>, t: Throwable) {
                loadingStatus.value = LoadingStatus.ERROR
                liveData.value = SearchResult.error()
            }
        })
    }

    fun getAggregatedItems(): List<ListItem> {
        return aggregatedItems
    }

    enum class LoadingStatus {
        SUCCESS,
        ERROR,
        LOADING
    }
}