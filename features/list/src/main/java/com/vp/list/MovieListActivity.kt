package com.vp.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MovieListActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Any>
    lateinit var searchView: SearchView
    private var searchViewExpanded = true
    private var searchText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, ListFragment(), ListFragment.TAG)
                .commit()
        } else {
            searchViewExpanded = savedInstanceState.getBoolean(IS_SEARCH_VIEW_ICONIFIED)
            searchText = savedInstanceState.getString(SEARCH_VIEW_TEXT)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        val menuItem: MenuItem = menu.findItem(R.id.search)
        searchView = menuItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        searchView.isIconified = searchViewExpanded
        searchView.setQuery(if (searchText != null) searchText else "", false)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val listFragment =
                    supportFragmentManager.findFragmentByTag(ListFragment.TAG) as ListFragment?
                listFragment!!.submitSearchQuery(query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_SEARCH_VIEW_ICONIFIED, searchView.isIconified)
        outState.putString(SEARCH_VIEW_TEXT, searchView.query.toString())
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingActivityInjector
    }

    companion object {
        private const val IS_SEARCH_VIEW_ICONIFIED = "is_search_view_iconified"
        private const val SEARCH_VIEW_TEXT = "search_view_text"
    }
}