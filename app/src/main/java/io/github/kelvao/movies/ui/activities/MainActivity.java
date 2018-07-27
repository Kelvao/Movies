package io.github.kelvao.movies.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.kelvao.movies.R;
import io.github.kelvao.movies.models.MovieListModel;
import io.github.kelvao.movies.presenters.MovieListPresenter;
import io.github.kelvao.movies.tasks.MovieList;
import io.github.kelvao.movies.tasks.OnLoadMoreListener;
import io.github.kelvao.movies.ui.adapters.MoviesAdapter;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.Callback, MovieList.View, OnLoadMoreListener {

    @BindView(R.id.tv_welcome)
    TextView tv_welcome;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    @BindView(R.id.rv_movies)
    RecyclerView rv_movies;
    @BindView(R.id.pg_movies)
    ProgressBar pg_movies;
    @BindView(R.id.cl_movies)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_toolbar)
    ConstraintLayout search_toolbar;
    @BindView(R.id.et_query)
    EditText et_query;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_close)
    ImageView iv_close;
    private MovieList.Presenter presenter;
    private MoviesAdapter adapter;
    private ArrayList<MovieListModel.Movie> movieList;
    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        movieList = new ArrayList<>();
        presenter = new MovieListPresenter(this);
        initSearchToolbar();
        initRecyclerView();
    }

    private void initSearchToolbar() {
        iv_back.setOnClickListener(view -> circleReveal(R.id.search_toolbar, 1, true, false));
        iv_close.setOnClickListener(view -> et_query.getText().clear());
        et_query.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!"".equals(et_query.getText().toString())) {
                    queryMovies(et_query.getText().toString());
                    hideSoftKeyboard();
                    return true;
                }
                return false;
            }
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        if (search_toolbar.getVisibility() == View.VISIBLE) {
            circleReveal(R.id.search_toolbar, 1, true, false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            circleReveal(R.id.search_toolbar, 1, true, true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("PrivateResource")
    public void circleReveal(int viewID, int posFromRight, boolean containsOverflow, final boolean isShow) {
        final View myView = findViewById(viewID);
        int width = myView.getWidth();
        if (posFromRight > 0) {
            width -= (posFromRight * getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)) - (getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2);
        }
        if (containsOverflow) {
            width -= getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);
        }
        int cx = width;
        int cy = myView.getHeight() / 2;
        Animator anim;
        if (isShow) {
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, (float) width);
            showSoftKeyboard();
            et_query.requestFocus();
        } else {
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float) width, 0);
            hideSoftKeyboard();
        }
        anim.setDuration((long) 220);
        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isShow) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            }
        });
        // make the view visible and start the animation
        if (isShow) {
            myView.setVisibility(View.VISIBLE);
        }
        // start the animation
        anim.start();
    }

    private void initRecyclerView() {
        rv_movies.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_movies.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_movies.getContext(),
                linearLayoutManager.getOrientation());
        rv_movies.addItemDecoration(dividerItemDecoration);
        adapter = new MoviesAdapter(movieList, rv_movies, this);
        adapter.setOnLoadMoreListener(this);
        rv_movies.setAdapter(adapter);
    }

    @Override
    public void onItemClick(MovieListModel.Movie movie) {
        Bundle bundle = new Bundle();
        bundle.putString("ImdbID", movie.getImdbID());
        startActivity(new Intent(MainActivity.this, MovieActivity.class).putExtras(bundle));
        finish();
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
    public void updateUi(Boolean isLoading) {
        tv_welcome.setVisibility(View.GONE);
        tv_empty.setVisibility(movieList.size() == 0 && !isLoading ? View.VISIBLE : View.GONE);
        rv_movies.setVisibility(movieList.size() == 0 || isLoading ? View.GONE : View.VISIBLE);
        pg_movies.setVisibility(isLoading ? View.VISIBLE : View.GONE);
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

    @Override
    public void onSuccess() {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Pesquisa concluída", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.WHITE);
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.BLACK);
        snackbar.show();
    }

    @Override
    public void onFailed(String message) {
        updateUi(false);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.WHITE);
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();
    }

    private void queryMovies(String title) {
        updateUi(true);
        clearMovieList();
        presenter.getMoviesList(title);
    }

    private void clearMovieList() {
        rv_movies.getLayoutManager().scrollToPosition(0);
        Log.i("TESTE", "queryMovies: ");
        int size = movieList.size();
        movieList.clear();
        adapter.notifyItemRangeRemoved(0, size);
    }

    public void showSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    public void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onLoadMore() {
        presenter.getMoreMovies();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getBoolean("firstrun", true)) {

            prefs.edit().putBoolean("firstrun", false).apply();
        }
    }
}
