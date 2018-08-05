package io.github.kelvao.movies.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class SoftKeyboardHelper {

    public static void closeKeyboard(@NonNull Context context) {
        InputMethodManager input = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        View view = ((Activity) context).getCurrentFocus();
        if (view != null && input != null) {
            input.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void openKeyboard(@NonNull Context context) {
        InputMethodManager input = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        View view = ((Activity) context).getCurrentFocus();
        if (view != null && input != null) {
            input.toggleSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_IMPLICIT, 0);
        }
    }
}
