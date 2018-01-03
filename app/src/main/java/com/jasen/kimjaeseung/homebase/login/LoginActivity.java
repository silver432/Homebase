package com.jasen.kimjaeseung.homebase.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jasen.kimjaeseung.homebase.main.MainActivity;

/**
 * Created by kimjaeseung on 2018. 1. 3..
 */

public class LoginActivity extends AppCompatActivity implements LoginPresenter.View {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 123;

    private LoginPresenterImpl loginPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.detachView();
        loginPresenter = null;
    }

    private void init() {
        loginPresenter = new LoginPresenterImpl();
        loginPresenter.attachView(this, this);


    }

    @Override
    public void goToMain() {
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
