package io.github.kelvao.movies.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import io.github.kelvao.movies.R;
import io.github.kelvao.movies.models.MovieListModel;
import io.github.kelvao.movies.ui.adapters.MoviesAdapter;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.Callback {

    @BindView(R.id.rv_movies)
    RecyclerView rv_movies;
    private MoviesAdapter adapter;
    private ArrayList<MovieListModel> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieList = new ArrayList<>();
        initRecyclerView();
    }

    private void initRecyclerView() {
        rv_movies.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_movies.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_movies.getContext(),
                linearLayoutManager.getOrientation());
        rv_movies.addItemDecoration(dividerItemDecoration);
        adapter = new MoviesAdapter(movieList, this);
        rv_movies.setAdapter(adapter);
    }

    @Override
    public void onItemClick(MovieListModel movie) {
        Bundle bundle = new Bundle();
        bundle.putString("ImdbID", movie.getImdbID());
        startActivity(new Intent(MainActivity.this, MovieActivity.class).putExtras(bundle));
        finish();
    }
}
