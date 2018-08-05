package io.github.kelvao.movies.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.kelvao.movies.R;
import io.github.kelvao.movies.models.MovieListModel;
import io.github.kelvao.movies.presenters.MovieListPresenter;
import io.github.kelvao.movies.tasks.MovieList;
import io.github.kelvao.movies.tasks.OnLoadMoreListener;
import io.github.kelvao.movies.ui.adapters.MovieAdapter;
import io.github.kelvao.movies.ui.utils.Snack;
import io.github.kelvao.movies.utils.Constants;

public class MovieListFragment extends Fragment implements MovieList.View, OnLoadMoreListener, MovieAdapter.Callback {

    @BindView(R.id.iv_empty)
    ImageView iv_empty;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    @BindView(R.id.rv_movies)
    RecyclerView rv_movies;
    @BindView(R.id.pg_movies)
    ProgressBar pg_movies;
    private MovieList.Presenter presenter;
    private MovieAdapter adapter;
    private ArrayList<MovieListModel.Movie> movieList;
    private MovieListCallback callback;

    public static MovieListFragment newInstance(String query) {
        Bundle args = new Bundle();
        args.putString(Constants.getQuery(), query);
        MovieListFragment fragment = new MovieListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment_movie_list = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this, fragment_movie_list);
        movieList = new ArrayList<>();
        presenter = new MovieListPresenter(this);
        initRecyclerView();
        if (getArguments() != null) {
            queryMovies(getArguments().getString(Constants.getQuery()));
        }
        return fragment_movie_list;
    }

    private void queryMovies(String title) {
        updateUi(true);
        clearMovieList();
        presenter.getMoviesList(title);
    }

    private void initRecyclerView() {
        rv_movies.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_movies.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_movies.getContext(),
                linearLayoutManager.getOrientation());
        rv_movies.addItemDecoration(dividerItemDecoration);
        adapter = new MovieAdapter(movieList, rv_movies, this);
        adapter.setOnLoadMoreListener(this);
        rv_movies.setAdapter(adapter);
    }

    @Override
    public void setMovieList(ArrayList<MovieListModel.Movie> movieList) {
        adapter.setLoaded();
        if (movieList != null) {
            this.movieList.remove(null);
            this.movieList.addAll(movieList);
            adapter.notifyDataSetChanged();
        }
        updateUi(false);
    }

    @Override
    public void onLoadMore() {
        presenter.getMoreMovies();
    }

    @Override
    public void onItemClick(MovieListModel.Movie movie) {
        callback.OpenMovieDetails(movie);
    }

    @Override
    public void runLayoutAnimation() {
        final Context context = rv_movies.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);
        rv_movies.setLayoutAnimation(controller);
        rv_movies.getAdapter().notifyDataSetChanged();
        rv_movies.scheduleLayoutAnimation();
    }

    private void clearMovieList() {
        rv_movies.getLayoutManager().scrollToPosition(0);
        int size = movieList.size();
        movieList.clear();
        adapter.notifyItemRangeRemoved(0, size);
    }

    @Override
    public void updateUi(Boolean isLoading) {
        tv_empty.setVisibility(movieList.size() == 0 && !isLoading ? View.VISIBLE : View.GONE);
        iv_empty.setVisibility(movieList.size() == 0 && !isLoading ? View.VISIBLE : View.GONE);
        rv_movies.setVisibility(movieList.size() == 0 || isLoading ? View.GONE : View.VISIBLE);
        pg_movies.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onSuccess() {
        if (getActivity() != null) {
            Snack.Success(getActivity());
        }
    }

    @Override
    public void onFailed(String message) {
        updateUi(false);
        if (getActivity() != null) {
            Snack.Error(getActivity(), message);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (MovieListCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement MovieListCallback");
        }

    }

    public interface MovieListCallback {
        void OpenMovieDetails(MovieListModel.Movie movie);
    }
}
