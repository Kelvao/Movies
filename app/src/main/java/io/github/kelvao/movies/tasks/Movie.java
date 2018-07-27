package io.github.kelvao.movies.tasks;

import io.github.kelvao.movies.models.MovieModel;

public interface Movie {
    interface View {
        void showMovie(MovieModel movie);

        void onSuccess();

        void onFailed(String message);
    }

    interface Presenter {
        void getMovie(String imdbID);
    }
}
