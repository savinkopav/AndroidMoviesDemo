package com.vp.favorites.di

import android.app.Application
import com.vp.detail.data.MovieRepositoryImpl
import com.vp.detail.data.local.db.MovieDatabase.Companion.getInstance
import com.vp.detail.domain.MovieRepository
import dagger.Module
import dagger.Provides

@Module
class FavoriteDataModule {

    @Provides
    fun providesMovieRepository(context: Application): MovieRepository {
        return MovieRepositoryImpl(getInstance(context))
    }
}