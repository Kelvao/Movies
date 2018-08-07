package io.github.kelvao.movies.ui.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.kelvao.movies.R;
import io.github.kelvao.movies.tasks.Animation;
import io.github.kelvao.movies.ui.adapters.SuggestionsAdapter;

public class AnimationsHelper {

    @BindView(R.id.search_toolbar)
    ConstraintLayout search_toolbar;
    @BindView(R.id.et_query)
    EditText et_query;
    @BindView(R.id.rv_suggestions)
    RecyclerView rv_suggestion;
    @BindView(R.id.abl_toolbar)
    AppBarLayout abl_toolbar;
    private Context context;

    public AnimationsHelper(Context context) {
        this.context = context;
        ButterKnife.bind(this, (Activity) context);
    }

    public void circleReveal(boolean isShow, ArrayList<String> suggestions, SuggestionsAdapter adapter) {
        if (isShow) {
            toolbarCircleReveal(true, () -> {
                if (suggestions.size() > 0) {
                    suggestionsCircleReveal(true, null);
                    adapter.filter(suggestions, et_query.getText().toString());
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
    private void toolbarCircleReveal(final boolean isShow, Animation animationCallback) {
        Resources resources = context.getResources();
        int width = search_toolbar.getWidth();
        width -= resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) - (resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 4);
        width -= resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);
        int cx = width;
        int cy = search_toolbar.getHeight() / 2;
        Animator anim;
        if (isShow) {
            et_query.requestFocus();
            SoftKeyboardHelper.openKeyboard(context);
            anim = ViewAnimationUtils.createCircularReveal(search_toolbar, cx, cy, 0, (float) width);
        } else {
            SoftKeyboardHelper.closeKeyboard(context);
            et_query.clearFocus();
            anim = ViewAnimationUtils.createCircularReveal(search_toolbar, cx, cy, (float) width, 0);
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

    public void suggestionsCircleReveal(boolean isShow, Animation animationCallback) {
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

    public void setCollapsingAnimation(final boolean isShow) {
        if (abl_toolbar.getHeight() - abl_toolbar.getBottom() == 0) {
            abl_toolbar.setExpanded(isShow, true);
        } else {
            abl_toolbar.setExpanded(false, false);
        }
        CoordinatorLayout.LayoutParams actionbar = (CoordinatorLayout.LayoutParams) abl_toolbar.getLayoutParams();
        final AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        if (isShow) {
            ValueAnimator valueAnimator = ValueAnimator.ofInt();
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(animation -> {
                behavior.setTopAndBottomOffset((Integer) animation.getAnimatedValue());
                abl_toolbar.requestLayout();
            });
            valueAnimator.setIntValues(-900, 900);
            valueAnimator.setDuration(600);
            valueAnimator.start();
        }
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return isShow;
            }
        });
        actionbar.setBehavior(behavior);
    }

}
