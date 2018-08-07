package io.github.kelvao.movies.ui.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import io.github.kelvao.movies.BuildConfig;
import io.github.kelvao.movies.R;
import io.github.kelvao.movies.models.MovieListModel;
import io.github.kelvao.movies.receivers.NetworkChangeReceiver;
import io.github.kelvao.movies.ui.adapters.SuggestionsAdapter;
import io.github.kelvao.movies.ui.fragments.MovieFragment;
import io.github.kelvao.movies.ui.fragments.MovieListFragment;
import io.github.kelvao.movies.ui.utils.AnimationsHelper;
import io.github.kelvao.movies.ui.utils.GlideApp;
import io.github.kelvao.movies.ui.utils.TapTargetViewHelper;
import io.github.kelvao.movies.utils.Constants;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener,
        SuggestionsAdapter.Callback, MovieListFragment.MovieListCallback, NetworkChangeReceiver.NetworkChange {

    @BindView(R.id.ctl_toolbar)
    CollapsingToolbarLayout ctl_toolbar;
    @BindView(R.id.iv_poster)
    ImageView iv_poster;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_toolbar)
    ConstraintLayout search_toolbar;
    @BindView(R.id.rv_suggestions)
    RecyclerView rv_suggestion;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.et_query)
    EditText et_query;
    @BindView(R.id.iv_clear)
    ImageView iv_clear;
    private MovieListFragment movieListFragment;
    private boolean showMenu = true;
    private SuggestionsAdapter adapter;
    private ArrayList<String> suggestions;
    private SharedPreferences sharedPreferences = null;
    private AnimationsHelper animationsHelper;
    private NetworkChangeReceiver networkChangeReceiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        sharedPreferences = getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        animationsHelper = new AnimationsHelper(this);
        animationsHelper.setCollapsingAnimation(false);
        suggestions = new ArrayList<>();
        initNetworkChangeReceiver();
        initFragment();
        initSearchToolbar();
        initSuggestionsRecyclerView();
    }

    private void initNetworkChangeReceiver() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        networkChangeReceiver = new NetworkChangeReceiver();
        networkChangeReceiver.setNetworkChange(this);
    }

    private void initFragment() {
        movieListFragment = MovieListFragment.newInstance(et_query.getText().toString());
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(0, 0, R.anim.slide_in_right, R.anim.slide_out_right)
                .replace(R.id.cfl_container, movieListFragment, Constants.MOVIE_LIST_FRAGMENT)
                .commit();
    }

    private void initSearchToolbar() {
        iv_back.setOnClickListener(view -> animationsHelper.circleReveal(false, suggestions, adapter));
        iv_clear.setOnClickListener(view -> et_query.getText().clear());
        et_query.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!"".equals(et_query.getText().toString())) {
                    initFragment();
                    if (!suggestions.contains(et_query.getText().toString().toLowerCase())) {
                        suggestions.add(et_query.getText().toString());
                    }
                    animationsHelper.circleReveal(false, suggestions, adapter);
                    return true;
                }
                return false;
            }
            return false;
        });
    }

    @OnTextChanged(value = R.id.et_query, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterQueryTextChanged(Editable editable) {
        if (editable.toString().isEmpty()) {
            iv_clear.setVisibility(View.INVISIBLE);
        } else {
            iv_clear.setVisibility(View.VISIBLE);
        }
        adapter.filter(suggestions, editable.toString());
    }

    private void initSuggestionsRecyclerView() {
        rv_suggestion.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_suggestion.setLayoutManager(linearLayoutManager);
        adapter = new SuggestionsAdapter(suggestions, this);
        rv_suggestion.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showMenu) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        onNextLayout(toolbar, this::checkFirstRun);
        return super.onCreateOptionsMenu(menu);
    }

    static void onNextLayout(final View view, final Runnable runnable) {
        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final ViewTreeObserver trueObserver;
                if (observer.isAlive()) {
                    trueObserver = observer;
                } else {
                    trueObserver = view.getViewTreeObserver();
                }
                removeOnGlobalLayoutListener(trueObserver, this);
                runnable.run();
            }
        });
    }

    static void removeOnGlobalLayoutListener(ViewTreeObserver observer,
                                             ViewTreeObserver.OnGlobalLayoutListener listener) {
        observer.removeOnGlobalLayoutListener(listener);
    }

    private void checkFirstRun() {
        if (sharedPreferences.getBoolean(Constants.getFirstRun(), true)) {
            TapTargetViewHelper tapTargetViewHelper = new TapTargetViewHelper(this);
            tapTargetViewHelper.showTutorial();
            sharedPreferences.edit().putBoolean(Constants.getFirstRun(), false).apply();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                popFragment();
                break;
            case R.id.action_search:
                animationsHelper.circleReveal(true, suggestions, adapter);
                return true;
            case R.id.action_about:
                startAboutActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startAboutActivity() {
        Intent intent = new LibsBuilder()
                .withAboutIconShown(true)
                .withAboutAppName(getResources().getString(R.string.app_name))
                .withActivityTitle(getResources().getString(R.string.about_title))
                .withAboutDescription(getResources().getString(R.string.about_description))
                .withAboutVersionShown(true)
                .withLicenseShown(true)
                .withVersionShown(true)
                .withAutoDetect(true)
                .withActivityStyle(Libs.ActivityStyle.DARK)
                .withLibraries(getResources().getStringArray(R.array.libraries))
                .intent(this);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            popFragment();
        } else {
            if (search_toolbar.getVisibility() == View.VISIBLE) {
                animationsHelper.circleReveal(false, suggestions, adapter);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onBackStackChanged() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (search_toolbar.getVisibility() == View.VISIBLE) {
                animationsHelper.circleReveal(false, suggestions, adapter);
            }
            animationsHelper.setCollapsingAnimation(true);
            showMenu = false;
            invalidateOptionsMenu();
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        } else {
            animationsHelper.setCollapsingAnimation(false);
            showMenu = true;
            invalidateOptionsMenu();
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        }
    }


    public void popFragment() {
        getSupportFragmentManager().popBackStackImmediate();
        getSupportFragmentManager().beginTransaction()
                .show(movieListFragment)
                .commit();
        ctl_toolbar.setTitle(getText(R.string.app_name));
    }

    @Override
    protected void onResume() {
        registerReceiver(networkChangeReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (search_toolbar.getVisibility() == View.VISIBLE) {
            animationsHelper.circleReveal(false, suggestions, adapter);
        }
        unregisterReceiver(networkChangeReceiver);
        super.onPause();
    }

    public void setCollapseInfo(String title, Uri imageUrl) {
        GlideApp.with(this)
                .load(imageUrl)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        animationsHelper.setCollapsingAnimation(false);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(iv_poster);
        ctl_toolbar.setTitle(title);
    }

    @Override
    public void onSuggestionClick(String suggestion) {
        et_query.setText(suggestion);
        initFragment();
        animationsHelper.circleReveal(false, suggestions, adapter);
    }

    @Override
    public void onSuggestionDelete(String suggestion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(suggestion);
        builder.setMessage(getText(R.string.delete_suggestion));
        builder.setPositiveButton(getText(R.string.delete), (dialogInterface, i) -> {
            suggestions.remove(suggestion);
            adapter.notifyDataSetChanged();
            if (suggestions.size() == 0) {
                animationsHelper.suggestionsCircleReveal(false, null);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void OpenMovieDetails(MovieListModel.Movie movie) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.cfl_container, MovieFragment.newInstance(movie.getImdbID()), Constants.MOVIE_FRAGMENT)
                .addToBackStack(Constants.MOVIE_FRAGMENT)
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_left)
                .hide(movieListFragment)
                .commit();
        setCollapseInfo(movie.getTitle(), movie.getPoster());
    }

    @Override
    public void OnNetworkChanged(int status) {
        if (getSupportFragmentManager().findFragmentByTag(Constants.MOVIE_LIST_FRAGMENT) == null
                && getSupportFragmentManager().findFragmentByTag(Constants.MOVIE_FRAGMENT) == null
                && status == Constants.NETWORK_CONNECTED) {
            initFragment();
        } else if (status == Constants.NETWORK_DESCONNECTED) {
            startActivity(new Intent(this, NetworkActivity.class));
            finish();
        }
        Log.i("TESTE", "OnNetworkChanged: " + status);
    }
}
