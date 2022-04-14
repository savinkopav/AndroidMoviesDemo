package com.vp.list

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridPagingScrollListener internal constructor(private val layoutManager: GridLayoutManager) : RecyclerView.OnScrollListener() {

    private var loadMoreItemsListener = EMPTY_LISTENER
    private var isLastPage = false
    private var isLoading = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (shouldLoadNextPage()) {
            loadMoreItemsListener.loadMoreItems(nextPageNumber)
        }
    }

    private fun shouldLoadNextPage(): Boolean {
        return isNotLoadingInProgress && userScrollsToNextPage() && isNotFirstPage && hasNextPage()
    }

    private fun userScrollsToNextPage(): Boolean {
        return layoutManager.childCount + layoutManager.findFirstVisibleItemPosition() >= layoutManager.itemCount
    }

    private val isNotFirstPage: Boolean
        get() = layoutManager.findFirstVisibleItemPosition() >= 0 && layoutManager.itemCount >= PAGE_SIZE

    private fun hasNextPage(): Boolean {
        return !isLastPage
    }

    private val isNotLoadingInProgress: Boolean
        get() = !isLoading
    private val nextPageNumber: Int
        get() = layoutManager.itemCount / PAGE_SIZE + 1

    fun setLoadMoreItemsListener(loadMoreItemsListener: LoadMoreItemsListener?) {
        if (loadMoreItemsListener != null) {
            this.loadMoreItemsListener = loadMoreItemsListener
        } else {
            this.loadMoreItemsListener = EMPTY_LISTENER
        }
    }

    fun markLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    fun markLastPage(isLastPage: Boolean) {
        this.isLastPage = isLastPage
    }

    interface LoadMoreItemsListener {
        fun loadMoreItems(page: Int)
    }

    companion object {
        private const val PAGE_SIZE = 10
        private val EMPTY_LISTENER: LoadMoreItemsListener =
            object : LoadMoreItemsListener {
                override fun loadMoreItems(page: Int) {

                }
            }
    }
}
