package io.github.kelvao.movies.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import io.github.kelvao.movies.R;
import io.github.kelvao.movies.tasks.Animation;
import io.github.kelvao.movies.ui.adapters.SuggestionsAdapter;
import io.github.kelvao.movies.ui.fragments.MovieListFragment;
import io.github.kelvao.movies.ui.utils.GlideApp;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener, SuggestionsAdapter.Callback {

    @BindView(R.id.abl_toolbar)
    AppBarLayout abl_toolbar;
    @BindView(R.id.ctl_toolbar)
    CollapsingToolbarLayout ctl_toolbar;
    @BindView(R.id.iv_poster)
    ImageView iv_poster;
    @BindView(R.id.v_shadow)
    View v_shadow;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        initFragment();
        initSearchToolbar();
        suggestions = new ArrayList<>();
        initRecyclerView();
    }

    private void initRecyclerView() {
        rv_suggestion.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_suggestion.setLayoutManager(linearLayoutManager);
        adapter = new SuggestionsAdapter(suggestions, this);
        rv_suggestion.setAdapter(adapter);
    }

    private void initFragment() {
        movieListFragment = MovieListFragment.newInstance(et_query.getText().toString());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.cfl_container, movieListFragment)
                .commit();
        v_shadow.setBackground(null);
    }


    private void initSearchToolbar() {
        iv_back.setOnClickListener(view -> circleReveal(false));
        iv_clear.setOnClickListener(view -> et_query.getText().clear());
        et_query.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!"".equals(et_query.getText().toString())) {
                    initFragment();
                    if (!suggestions.contains(et_query.getText().toString().toLowerCase())) {
                        suggestions.add(et_query.getText().toString());
                    }
                    circleReveal(false);
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
        final ArrayList<String> filteredList = filter(suggestions, editable.toString());
        adapter.setfilter(filteredList);
    }

    private ArrayList<String> filter(ArrayList<String> suggestions, String query) {
        query = query.toLowerCase();
        final ArrayList<String> filteredModeList = new ArrayList<>();
        for (String suggestion : suggestions) {
            if (suggestion.startsWith(query)) {
                filteredModeList.add(suggestion);
            }
        }
        return filteredModeList;
    }

    private void circleReveal(boolean isShow) {
        if (isShow) {
            toolbarCircleReveal(true, () -> {
                if (suggestions.size() > 0) {
                    suggestionsCircleReveal(true, null);
                    final ArrayList<String> filteredList = filter(suggestions, et_query.getText().toString());
                    adapter.setfilter(filteredList);
                }
            });
        } else {
            if (suggestions.size() > 0) {
                suggestionsCircleReveal(false, () -> toolbarCircleReveal(false, null));
            } else {
                toolbarCircleReveal(false, null);
            }
        }
    }

    @SuppressLint("PrivateResource")
    public void toolbarCircleReveal(final boolean isShow, Animation animationCallback) {
        int width = search_toolbar.getWidth();
        width -= getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) - (getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 4);
        width -= getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);
        int cx = width;
        int cy = search_toolbar.getHeight() / 2;
        Animator anim;
        if (isShow) {
            anim = ViewAnimationUtils.createCircularReveal(search_toolbar, cx, cy, 0, (float) width);
            showSoftKeyboard();
            et_query.requestFocus();
        } else {
            anim = ViewAnimationUtils.createCircularReveal(search_toolbar, cx, cy, (float) width, 0);
            hideSoftKeyboard();
            et_query.clearFocus();
        }
        anim.setDuration((long) 220);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isShow) {
                    super.onAnimationEnd(animation);
                    search_toolbar.setVisibility(View.INVISIBLE);
                }
                if (animationCallback != null) {
                    animationCallback.animationEnded();
                }
            }
        });
        if (isShow) {
            search_toolbar.setVisibility(View.VISIBLE);
        }
        anim.start();
    }

    private void suggestionsCircleReveal(boolean isShow, Animation animationCallback) {
        int finalRadius = rv_suggestion.getHeight();
        int cy = 0;
        int cx = rv_suggestion.getWidth() / 2;
        Animator anim;
        if (isShow) {
            anim = ViewAnimationUtils.createCircularReveal(rv_suggestion, cx, cy, 0, finalRadius);
        } else {
            anim = ViewAnimationUtils.createCircularReveal(rv_suggestion, cx, cy, finalRadius, 0);
        }
        anim.setDuration((long) 220);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isShow) {
                    super.onAnimationEnd(animation);
                    rv_suggestion.setVisibility(View.INVISIBLE);
                }
                if (animationCallback != null) {
                    animationCallback.animationEnded();
                }
            }
        });
        if (isShow) {
            rv_suggestion.setVisibility(View.VISIBLE);
        }
        anim.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showMenu) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                popFragment();
                break;
            case R.id.action_search:
                circleReveal(true);
                return true;
            case R.id.action_about:
                Intent intent = new LibsBuilder()
                        .withAboutIconShown(true)
                        .withAboutAppName(getResources().getString(R.string.app_name))
                        .withActivityTitle(getResources().getString(R.string.about))
                        .withAboutVersionShown(true)
                        .withLicenseShown(true)
                        .withVersionShown(true)
                        .withAutoDetect(true)
                        .withActivityStyle(Libs.ActivityStyle.DARK)
                        .withLibraries("DesertIcon")
                        .intent(this);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    public void hideSoftKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            popFragment();
        } else {
            if (search_toolbar.getVisibility() == View.VISIBLE) {
                circleReveal(true);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onBackStackChanged() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (search_toolbar.getVisibility() == View.VISIBLE) {
                circleReveal(false);
            }
            abl_toolbar.setExpanded(true, true);
            showMenu = false;
            invalidateOptionsMenu();
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        } else {
            abl_toolbar.setExpanded(false, true);
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
    protected void onPause() {
        if (search_toolbar.getVisibility() == View.VISIBLE) {
            circleReveal(false);
        }
        super.onPause();
    }

    public void setCollapseInfo(String title, Uri imageUrl) {
        imageUrl = Uri.parse(String.valueOf(imageUrl).replace("SX300", "SX500"));
        GlideApp.with(this)
                .load(imageUrl)
                .centerCrop()
                .error(getDrawable(R.drawable.ic_broken_image))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(iv_poster);
        ctl_toolbar.setTitle(title);
        v_shadow.setBackground(getDrawable(R.drawable.collapsed_image_background));
    }

    @Override
    public void onSuggestionClick(String suggestion) {
        et_query.setText(suggestion);
        initFragment();
        circleReveal(false);
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
                suggestionsCircleReveal(false, null);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }
}
