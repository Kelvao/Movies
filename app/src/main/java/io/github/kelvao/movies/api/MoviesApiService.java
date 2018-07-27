package io.github.kelvao.movies.api;

import java.util.HashMap;

import io.github.kelvao.movies.models.MovieListModel;
import io.github.kelvao.movies.models.MovieModel;
import io.github.kelvao.movies.utils.Constants;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

public interface MoviesApiService {
    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json"
    })
    @GET(Constants.BASE_URL)
    Call<MovieListModel> getMovieList(@QueryMap HashMap<String, String> params);

    @GET(Constants.BASE_URL)
    Call<MovieModel> getMovie(@QueryMap HashMap<String, String> params);
}
