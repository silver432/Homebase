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
 * Created by kimjaeseung on 2018. 1. 15..
 */

public class SignUpActivity2 extends AppCompatActivity {
    private static final String TAG = SignUpActivity2.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        ButterKnife.bind(this);

        init();
    }

    private void init() {

    }

    @OnClick({R.id.signup2_btn_register_team, R.id.signup2_btn_join_team})
    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.signup2_btn_register_team:
                goToSignUp3();
                break;
            case R.id.signup2_btn_join_team:

                break;
        }
    }

    private void goToSignUp3() {
        final Intent intent = new Intent(this, SignUpActivity3.class);
        startActivity(intent);
        finish();
    }
}
