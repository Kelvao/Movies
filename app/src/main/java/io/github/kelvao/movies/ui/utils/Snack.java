package io.github.kelvao.movies.ui.utils;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import io.github.kelvao.movies.R;

public class Snack {


    public static void Success(Activity activity) {
        Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.cl_movies), activity.getText(R.string.search_success), Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.WHITE);
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.BLACK);
        snackbar.show();
    }

    public static void Error(Activity activity, String message) {
        Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.cl_movies), message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.WHITE);
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();
    }

}
