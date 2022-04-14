package com.vp.favorites.di

import com.vp.favorites.FavoriteActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoriteActivityModule {
    @ContributesAndroidInjector(modules = [FavoriteViewModelsModule::class, FavoriteDataModule::class])
    abstract fun bindFavoriteActivity(): FavoriteActivity
}