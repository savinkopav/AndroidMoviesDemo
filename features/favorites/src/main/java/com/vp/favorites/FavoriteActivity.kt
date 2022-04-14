package com.vp.favorites

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vp.detail.data.local.db.model.Movie
import com.vp.favorites.viewmodel.FavoriteAdapter
import com.vp.favorites.viewmodel.FavoriteViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class FavoriteActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoriteRecyclerView: RecyclerView
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        initRecyclerView()
        initAdapter()
        initViewModels()

        favoriteViewModel.movies().observe(this, Observer {
            onResult(it)
        })

        favoriteViewModel.getMovies()
    }

    private fun initRecyclerView() {
        favoriteRecyclerView = findViewById(R.id.favoriteRecyclerView)
        favoriteRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)
        favoriteRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }

    private fun initAdapter() {
        adapter = FavoriteAdapter(object : FavoriteAdapter.OnItemClickListener {
            override fun onItemClick(imdbID: String) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail"))
                intent.setPackage(packageName)
                intent.putExtra("imdbID", imdbID)
                intent.putExtra("source", "favorite")
                startActivity(intent)
            }
        })
        favoriteRecyclerView.adapter = adapter
    }

    private fun initViewModels() {
        favoriteViewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel::class.java)
    }

    private fun onResult(result: List<Movie>) {
        adapter.setItems(result)
    }
}