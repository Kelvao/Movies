package io.github.kelvao.movies.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;
import java.util.Objects;

import io.github.kelvao.movies.api.MoviesApiService;
import io.github.kelvao.movies.models.MovieListModel;
import io.github.kelvao.movies.tasks.MovieList;
import io.github.kelvao.movies.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieListPresenter implements MovieList.Presenter {

    private static final String TAG = "TESTE";
    private String title;
    private MovieList.View view;
    private Call<MovieListModel> call;
    private MoviesApiService moviesApiService;
    private static final String API_KEY = "45d162c8";

    public MovieListPresenter(MovieList.View view) {
        this.view = view;
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        moviesApiService = retrofit.create(MoviesApiService.class);
    }

    @Override
    public void getMovie(String title) {

    }

    @Override
    public void getMoviesList(String title) {
        this.title = title;
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.SEARCH, title);
        params.put(Constants.TYPE, "movie");
        params.put(Constants.API_KEY, API_KEY);
        call = moviesApiService.getVideosList(params);
        call.enqueue(new Callback<MovieListModel>() {
            @Override
            public void onResponse(@NonNull Call<MovieListModel> call, @NonNull Response<MovieListModel> response) {
                MovieListModel movieList = response.body();
                if (response.isSuccessful()) {
                    view.setMovieList(Objects.requireNonNull(movieList).getMovies());
                    view.onSuccess();
                } else {
                    view.onFailed(response.message());
                    Log.i(TAG, "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieListModel> call, @NonNull Throwable t) {
                view.onFailed(t.getMessage());
                Log.i(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void getMoreMovies() {

    }
}
