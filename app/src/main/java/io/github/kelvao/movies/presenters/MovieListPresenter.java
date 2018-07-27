package io.github.kelvao.movies.presenters;

import android.support.annotation.NonNull;

import java.util.HashMap;

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

    private MovieList.View view;
    private MoviesApiService moviesApiService;
    private int page = 1;
    private int numPages = 2;
    private String query = "";
    private HashMap<String, String> params;

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
    public void getMoviesList(String title) {
        initPagination(title);
        initParams();
        Call<MovieListModel> call = moviesApiService.getMovieList(params);
        call.enqueue(new Callback<MovieListModel>() {
            @Override
            public void onResponse(@NonNull Call<MovieListModel> call, @NonNull Response<MovieListModel> response) {
                MovieListModel movieList = response.body();
                if (response.isSuccessful() && movieList != null) {
                    numPages = movieList.getTotalResults() / 10;
                    if (movieList.getTotalResults() % 10 != 0) {
                        numPages++;
                    }
                    page++;
                    if (page < numPages) {
                        movieList.getMovies().add(null);
                    }
                    view.setMovieList(movieList.getMovies());
                    view.runLayoutAnimation();
                    view.onSuccess();
                } else {
                    view.onFailed(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieListModel> call, @NonNull Throwable t) {
                view.onFailed(t.getMessage());
            }
        });
    }

    @Override
    public void getMoreMovies() {
        if (page <= numPages) {
            initParams();
            Call<MovieListModel> call = moviesApiService.getMovieList(params);
            call.enqueue(new Callback<MovieListModel>() {
                @Override
                public void onResponse(@NonNull Call<MovieListModel> call, @NonNull Response<MovieListModel> response) {
                    MovieListModel movieList = response.body();
                    if (response.isSuccessful() && movieList != null) {
                        page++;
                        if (page < numPages) {
                            movieList.getMovies().add(null);
                        }
                        view.setMovieList(movieList.getMovies());
                    } else {
                        view.onFailed(response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MovieListModel> call, @NonNull Throwable t) {
                    view.onFailed(t.getMessage());
                }
            });
        }
    }

    private void initParams() {
        params = new HashMap<>();
        params.put(Constants.getParamSearch(), query);
        params.put(Constants.getParamType(), "movie");
        params.put(Constants.getParamPage(), String.valueOf(page));
        params.put(Constants.getParamApiKey(), Constants.getApiKey());
    }

    private void initPagination(String title) {
        page = 1;
        numPages = 2;
        query = title;
    }
}
