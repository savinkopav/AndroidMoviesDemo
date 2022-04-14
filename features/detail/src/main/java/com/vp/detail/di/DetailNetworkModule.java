package com.vp.detail.di;

import android.app.Application;
import com.vp.detail.data.MovieRepositoryImpl;
import com.vp.detail.data.local.db.MovieDatabase;
import com.vp.detail.domain.MovieRepository;
import com.vp.detail.service.DetailService;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class DetailNetworkModule {

    @Provides
    DetailService providesDetailService(Retrofit retrofit) {
        return retrofit.create(DetailService.class);
    }

    @Provides
    MovieRepository providesMovieRepository(Application context) {
        return new MovieRepositoryImpl(MovieDatabase.getInstance(context));
    }
}