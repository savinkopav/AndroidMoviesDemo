package com.vp.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var detailViewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        queryProvider = this
        binding.lifecycleOwner = this
        detailViewModel.fetchDetails()
        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
        detailViewModel.insertingState().observe(this, Observer { state ->
            when (state) {
                DetailsViewModel.InsertingDatabaseState.SUCCESS -> {
                    Toast.makeText(this@DetailActivity, getString(R.string.detail_view_message_item_saved), Toast.LENGTH_SHORT).show()
                }
                DetailsViewModel.InsertingDatabaseState.ERROR -> {
                   Toast.makeText(this@DetailActivity, getString(R.string.detail_view_message_item_not_saved), Toast.LENGTH_SHORT).show()
                }
                DetailsViewModel.InsertingDatabaseState.IN_PROGRESS -> {}
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        val favoriteButton = menu?.findItem(R.id.star)
        favoriteButton?.let {
            if (getQuerySource() == "favorite") {
                it.isVisible = false
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.star -> {

                detailViewModel.details().value?.let { movieDetail ->
                    detailViewModel.saveMovie(movieDetail, queryProvider.getMovieId())
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun getMovieId(): String {
        return intent?.extras?.getString("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }

    override fun getQuerySource(): String {
        return intent?.extras?.getString("source") ?: run {
            return ""
        }
    }

    companion object {
        lateinit var queryProvider: QueryProvider
    }
}
