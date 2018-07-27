package io.github.kelvao.movies.tasks;

import java.util.ArrayList;

import io.github.kelvao.movies.models.MovieListModel;

public interface MovieList {
    interface View {
        void setMovieList(ArrayList<MovieListModel.Movie> movieList);

        void updateUi(Boolean isLoading);

        void onSuccess();

        void onFailed(String message);
    }

    interface Presenter {
        void getMovie(String title);

        void getMoviesList(String title);

        void getMoreMovies();
    }
}
