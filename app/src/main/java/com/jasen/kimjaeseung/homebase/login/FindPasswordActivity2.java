package com.jasen.kimjaeseung.homebase.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jasen.kimjaeseung.homebase.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kimjaeseung on 2018. 1. 26..
 */

public class FindPasswordActivity2 extends AppCompatActivity {
    private static final String TAG = FindPasswordActivity2.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password2);

        ButterKnife.bind(this);

    }

    @OnClick({R.id.find_password2_btn_goto_login})
    public void mOnClick(View view){
        switch (view.getId()){
            case R.id.find_password2_btn_goto_login:
                goToLogin();
                break;
        }
    }

    private void goToLogin() {
        final Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
