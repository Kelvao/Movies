package io.github.kelvao.movies.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.github.kelvao.movies.ui.utils.NetworkUtil;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private static NetworkChange networkChange;

    @Override
    public void onReceive(Context context, Intent intent) {
        networkChange.OnNetworkChanged(NetworkUtil.getConnectivityStatus(context));
    }

    public void setNetworkChange(NetworkChange networkChange) {
        NetworkChangeReceiver.networkChange = networkChange;
    }

    public interface NetworkChange {
        void OnNetworkChanged(int status);
    }

}
