package io.github.kelvao.movies;

import com.orhanobut.hawk.Hawk;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        Hawk.init(getApplicationContext()).build();
        super.onCreate();
    }
}
