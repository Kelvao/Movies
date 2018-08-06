package io.github.kelvao.movies.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.kelvao.movies.BuildConfig;
import io.github.kelvao.movies.R;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.tv_version)
    TextView tv_version;
    private final Handler waitHandler = new Handler();
    private Runnable waitCallbackMain = () -> {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        tv_version.setText(String.format("v%s", BuildConfig.VERSION_NAME));
        waitHandler.postDelayed(waitCallbackMain, 3000);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
