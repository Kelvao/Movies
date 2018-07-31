package io.github.kelvao.movies.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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
import com.orhanobut.hawk.Hawk;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import io.github.kelvao.movies.R;
import io.github.kelvao.movies.ui.fragments.MovieListFragment;
import io.github.kelvao.movies.ui.utils.GlideApp;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

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
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.et_query)
    EditText et_query;
    @BindView(R.id.iv_clear)
    ImageView iv_clear;
    private MovieListFragment movieListFragment;
    private boolean showMenu = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        movieListFragment = MovieListFragment.newInstance(null);
        initFragment();
        initSearchToolbar();
        initHawk();
    }

    private void initHawk() {
        Hawk.init(this).build();
    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.cfl_container, MovieListFragment.newInstance(et_query.getText().toString()))
                .commit();
    }


    private void initSearchToolbar() {
        iv_back.setOnClickListener(view -> circleReveal(false));
        iv_clear.setOnClickListener(view -> et_query.getText().clear());
        et_query.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!"".equals(et_query.getText().toString())) {
                    initFragment();
                    hideSoftKeyboard();
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
    }

    @SuppressLint("PrivateResource")
    public void circleReveal(final boolean isShow) {
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
        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isShow) {
                    super.onAnimationEnd(animation);
                    search_toolbar.setVisibility(View.INVISIBLE);
                    search_toolbar.bringToFront();
                    search_toolbar.invalidate();
                }
            }
        });
        // make the view visible and start the animation
        if (isShow) {
            search_toolbar.setVisibility(View.VISIBLE);
        }
        // start the animation
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
                new LibsBuilder()
                        .withAboutIconShown(true)
                        .withAboutAppName(getResources().getString(R.string.app_name))
                        .withActivityTitle(getResources().getString(R.string.about))
                        .withAboutVersionShown(true)
                        .withLicenseShown(true)
                        .withVersionShown(true)
                        .withAutoDetect(true)
                        .withActivityStyle(Libs.ActivityStyle.DARK)
                        .start(this);
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
                circleReveal(false);
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

}
