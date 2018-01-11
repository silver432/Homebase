package com.jasen.kimjaeseung.homebase.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.main.MainActivity;
import com.jasen.kimjaeseung.homebase.util.BaseTextWatcher;
import com.jasen.kimjaeseung.homebase.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kimjaeseung on 2018. 1. 7..
 */

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = SignUpActivity.class.getSimpleName();

    @BindView(R.id.signup_et_email)
    TextInputEditText emailEditText;
    @BindView(R.id.signup_et_password)
    TextInputEditText passwordEditText;
    @BindView(R.id.signup_et_password_confirm)
    TextInputEditText passwordConfirmEditText;
    @BindView(R.id.signup_til_email)
    TextInputLayout emailTextInputLayout;
    @BindView(R.id.signup_til_password)
    TextInputLayout passwordTextInputLayout;
    @BindView(R.id.signup_til_password_confirm)
    TextInputLayout passwordTextInputLayoutConfirm;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        emailEditText.addTextChangedListener(new BaseTextWatcher(this,emailTextInputLayout,emailEditText,null));
        passwordConfirmEditText.addTextChangedListener(new BaseTextWatcher(this,passwordTextInputLayoutConfirm,passwordConfirmEditText,passwordEditText));

        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick({R.id.signup_btn_back, R.id.signup_btn_confirm})
    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.signup_btn_back:
                goToLogin();
                break;
            case R.id.signup_btn_confirm:
                confirmSignUp();
                break;
        }
    }

    private void confirmSignUp() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordConfirm = passwordConfirmEditText.getText().toString();

        if (email.isEmpty()||password.isEmpty()||passwordConfirm.isEmpty()||!password.equals(passwordConfirm)) {
            ToastUtils.showToast(this,getString(R.string.login_failure));
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success");
                    goToLogin();
                } else {
                    ToastUtils.showToast(getApplicationContext(),getString(R.string.signup_failure));
                }
            }
        });
    }

    protected void goToLogin() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
