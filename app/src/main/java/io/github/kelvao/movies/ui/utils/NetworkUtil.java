package io.github.kelvao.movies.ui.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import java.util.Objects;

import io.github.kelvao.movies.ui.activities.NetworkActivity;
import io.github.kelvao.movies.utils.Constants;

public class NetworkUtil {

    public static int getConnectivityStatus(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();
        return activeNetwork == null || !activeNetwork.isConnected() ?
                Constants.NETWORK_DESCONNECTED : Constants.NETWORK_CONNECTED;
    }

}
