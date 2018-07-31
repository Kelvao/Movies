package io.github.kelvao.movies.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.kelvao.movies.R;
import io.github.kelvao.movies.models.MovieModel;
import io.github.kelvao.movies.presenters.MoviePresenter;
import io.github.kelvao.movies.tasks.Movie;
import io.github.kelvao.movies.ui.activities.MainActivity;
import io.github.kelvao.movies.ui.adapters.GenreAdapter;
import io.github.kelvao.movies.utils.Constants;

public class MovieFragment extends Fragment implements Movie.View {

    @BindView(R.id.tv_released)
    TextView tv_released;
    @BindView(R.id.tv_director)
    TextView tv_director;
    @BindView(R.id.tv_plot)
    TextView tv_plot;
    @BindView(R.id.rv_genre)
    RecyclerView rv_genre;
    private ArrayList<String> genres;

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
        genres = new ArrayList<>();
        Movie.Presenter presenter = new MoviePresenter(this);
        presenter.getMovie(Objects.requireNonNull(getArguments()).getString(Constants.getImdbId()));
        return fragment_movie;
    }

    @Override
    public void showMovie(MovieModel movie) {
       // ((MainActivity) Objects.requireNonNull(getActivity()))
         //       .setCollapseInfo(movie.getTitle(), movie.getPoster());
        tv_released.setText(String.format("%s: %s", getText(R.string.year), movie.getReleased()));
        tv_director.setText(String.format("%s: %s", getText(R.string.director), movie.getDirector()));
        tv_plot.setText(movie.getPlot());
        genres.addAll(movie.getGenre());
        initRecyclerView();
    }

    public void initRecyclerView() {
        rv_genre.setHasFixedSize(true);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        rv_genre.setLayoutManager(layoutManager);
        GenreAdapter adapter = new GenreAdapter(genres);
        rv_genre.setAdapter(adapter);
    }

    @Override
    public void onFailed(String message) {

    }
}
