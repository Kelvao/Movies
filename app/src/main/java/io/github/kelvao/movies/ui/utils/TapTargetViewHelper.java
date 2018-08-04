package io.github.kelvao.movies.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.support.v7.widget.Toolbar;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.kelvao.movies.R;

public class TapTargetViewHelper {

    @BindView(R.id.toolbar) Toolbar toolbar;
    private Activity activity;
    private Context context;

    public TapTargetViewHelper(Activity activity) {
        this.activity = activity;
        this.context = activity.getBaseContext();
        ButterKnife.bind(this, activity);
    }

    public void showTutorial() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        int screenHeight = displaymetrics.heightPixels;
        final Drawable movie = ContextCompat.getDrawable(context, R.drawable.ic_movie);
        final Rect movieTarget = new Rect(0, 0,
                (movie != null ? movie.getIntrinsicWidth() : 0 ) * 2,
                (movie != null ? movie.getIntrinsicHeight() : 0) * 2);
        movieTarget.offset(screenWidth / 2, screenHeight / 2);
        new TapTargetSequence(activity)
                .continueOnCancel(true)
                .targets(
                        TapTarget.forToolbarMenuItem(toolbar, R.id.action_search,
                                context.getText(R.string.tutorial_search_title), context.getText(R.string.tutorial_search_description))
                                .targetCircleColor(android.R.color.white)
                                .titleTextColor(android.R.color.white)
                                .descriptionTextColor(android.R.color.white)
                                .descriptionTextAlpha(0.4f)
                                .drawShadow(true)
                                .cancelable(true)
                                .tintTarget(true),
                        TapTarget.forToolbarMenuItem(toolbar, R.id.action_about,
                                context.getText(R.string.tutorial_about_title), context.getText(R.string.tutorial_about_description))
                                .targetCircleColor(android.R.color.white)
                                .titleTextColor(android.R.color.white)
                                .descriptionTextColor(android.R.color.white)
                                .descriptionTextAlpha(0.4f)
                                .drawShadow(true)
                                .cancelable(true)
                                .tintTarget(true),
                        TapTarget.forBounds(movieTarget,
                                context.getText(R.string.tutorial_list_title), context.getText(R.string.tutorial_list_description))
                                .targetCircleColor(android.R.color.white)
                                .titleTextColor(android.R.color.white)
                                .descriptionTextColor(android.R.color.white)
                                .descriptionTextAlpha(0.4f)
                                .icon(movie)
                                .cancelable(true)
                                .tintTarget(true)
                ).start();
    }
}
