package com.jasen.kimjaeseung.homebase.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jasen.kimjaeseung.homebase.main.MainActivity;

/**
 * Created by kimjaeseung on 2018. 1. 6..
 */

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }
}
