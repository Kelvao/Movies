package io.github.kelvao.movies.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import butterknife.ButterKnife;
import io.github.kelvao.movies.R;
import io.github.kelvao.movies.models.MovieModel;
import io.github.kelvao.movies.presenters.MoviePresenter;
import io.github.kelvao.movies.tasks.Movie;
import io.github.kelvao.movies.utils.Constants;

public class MovieFragment extends Fragment implements Movie.View {

    public static MovieFragment newInstance(String imdbID) {
        Bundle args = new Bundle();
        args.putString(Constants.getImdbId(), imdbID);
        MovieFragment fragment = new MovieFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment_movie = inflater.inflate(R.layout.fragment_movie, container, false);
        ButterKnife.bind(this, fragment_movie);
        setHasOptionsMenu(true);
        Movie.Presenter presenter = new MoviePresenter(this);
        presenter.getMovie(Objects.requireNonNull(getArguments()).getString(Constants.getImdbId()));
        return fragment_movie;
    }

    @Override
    public void showMovie(MovieModel movie) {

    }

    @Override
    public void onFailed(String message) {

    }
}
