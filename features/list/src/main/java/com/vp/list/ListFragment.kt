package com.vp.list

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ViewAnimator
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vp.list.GridPagingScrollListener.LoadMoreItemsListener
import com.vp.list.model.ListItem
import com.vp.list.viewmodel.ListState
import com.vp.list.viewmodel.ListViewModel
import com.vp.list.viewmodel.SearchResult
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ListFragment : Fragment(), LoadMoreItemsListener, ListAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var listViewModel: ListViewModel
    lateinit var gridPagingScrollListener: GridPagingScrollListener
    lateinit var listAdapter: ListAdapter
    lateinit var viewAnimator: ViewAnimator
    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var progressBar: ProgressBar
    lateinit var errorTextView: TextView
    lateinit var loadingStatus: ProgressBar
    private var currentQuery: String = "Interview"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        listViewModel =
            ViewModelProviders.of(this, factory).get(ListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        viewAnimator = view.findViewById(R.id.viewAnimator)
        progressBar = view.findViewById(R.id.progressBar)
        errorTextView = view.findViewById(R.id.errorText)
        loadingStatus = view.findViewById(R.id.loadingStatus)

        savedInstanceState?.getString(CURRENT_QUERY)?.let {
            currentQuery = it
        }

        initBottomNavigation(view)
        initList()
        listViewModel.observeMovies().observe(viewLifecycleOwner) { searchResult: SearchResult? ->
            if (searchResult != null) {
                handleResult(listAdapter, searchResult)
            }
        }
        listViewModel.loadingStatus().observe(viewLifecycleOwner) { status ->
            showHasActiveDownload(status)
        }
        if (listViewModel.getAggregatedItems().isEmpty()) {
            listViewModel.searchMoviesByTitle(currentQuery, 1)
            showProgressBar()
        }
        swipeRefreshLayout.setOnRefreshListener {
            listAdapter.clearItems()
            listViewModel.searchMoviesByTitle(currentQuery, 1)
        }
    }

    private fun initBottomNavigation(view: View) {
        val bottomNavigationView: BottomNavigationView = view.findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            if (item.itemId == R.id.favorites) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/favorites"))
                intent.setPackage(requireContext().packageName)
                startActivity(intent)
            }
            true
        }
    }

    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    private fun initList() {
        listAdapter = ListAdapter()
        listAdapter.setOnItemClickListener(this)
        recyclerView.adapter = listAdapter
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(
            context,
            if (resources.configuration.orientation === Configuration.ORIENTATION_PORTRAIT) 2 else 3
        )
        recyclerView.layoutManager = layoutManager

        // Pagination
        gridPagingScrollListener = GridPagingScrollListener(layoutManager)
        gridPagingScrollListener.setLoadMoreItemsListener(this)
        recyclerView.addOnScrollListener(gridPagingScrollListener)
    }

    private fun showProgressBar() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(progressBar)
    }

    private fun showList() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(recyclerView)
    }

    private fun showError() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(errorTextView)
    }

    private fun handleResult(listAdapter: ListAdapter, searchResult: SearchResult) {
        when (searchResult.listState) {
            ListState.LOADED -> {
                swipeRefreshLayout.isRefreshing = false
                setItemsData(listAdapter, searchResult)
                showList()
            }
            ListState.IN_PROGRESS -> {
                showProgressBar()
            }
            else -> {
                swipeRefreshLayout.isRefreshing = false
                showError()
            }
        }
        gridPagingScrollListener.markLoading(false)
    }

    private fun showHasActiveDownload(status: ListViewModel.LoadingStatus) {
        when (status) {
            ListViewModel.LoadingStatus.SUCCESS -> {
                loadingStatus.visibility = View.GONE
            }
            ListViewModel.LoadingStatus.LOADING -> {
                loadingStatus.visibility = View.VISIBLE
            }
            else -> {
                loadingStatus.visibility = View.GONE
            }
        }
    }

    private fun setItemsData(listAdapter: ListAdapter, searchResult: SearchResult) {
        listAdapter.setItems(searchResult.getItems() as MutableList<ListItem>)
        if (searchResult.totalResult <= listAdapter.itemCount) {
            gridPagingScrollListener.markLastPage(true)
        } else {
            gridPagingScrollListener.markLastPage(false)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_QUERY, currentQuery)
    }

    override fun loadMoreItems(page: Int) {
        gridPagingScrollListener.markLoading(true)
        listViewModel.searchMoviesByTitle(currentQuery, page)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitSearchQuery(query: String) {
        currentQuery = query
        listAdapter.clearItems()
        listAdapter.notifyDataSetChanged()
        listViewModel.searchMoviesByTitle(query, 1)
        showProgressBar()
    }

    override fun onItemClick(imdbID: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail"))
        intent.setPackage(requireContext().packageName)
        intent.putExtra("imdbID", imdbID)
        startActivity(intent)
    }

    companion object {
        const val TAG = "ListFragment"
        private const val CURRENT_QUERY = "current_query"
    }
}