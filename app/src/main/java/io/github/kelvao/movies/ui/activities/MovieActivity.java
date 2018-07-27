package io.github.kelvao.movies.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Objects;

import io.github.kelvao.movies.R;
import io.github.kelvao.movies.models.MovieModel;
import io.github.kelvao.movies.presenters.MoviePresenter;
import io.github.kelvao.movies.tasks.Movie;

public class MovieActivity extends AppCompatActivity implements Movie.View {

    private Movie.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        presenter = new MoviePresenter(this);
        Bundle bundle = getIntent().getExtras();
        presenter.getMovie(Objects.requireNonNull(bundle).getString("imdbID"));
    }

    @Override
    public void showMovie(MovieModel movie) {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailed(String message) {

    }

    @Override
    public void onBackPressed() {
        exit();
    }

    public void exit() {

    }
}
