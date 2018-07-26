package io.github.kelvao.movies.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.kelvao.movies.R;

public class MovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
    }
}
