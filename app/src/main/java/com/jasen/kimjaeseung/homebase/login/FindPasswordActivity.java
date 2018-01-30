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

import com.google.android.gms.auth.TokenData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.util.BaseTextWatcher;
import com.jasen.kimjaeseung.homebase.util.RegularExpressionUtils;
import com.jasen.kimjaeseung.homebase.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kimjaeseung on 2018. 1. 26..
 */

public class FindPasswordActivity extends AppCompatActivity {
    private static final String TAG = FindPasswordActivity.class.getSimpleName();

    @BindView(R.id.find_password_til_name)
    TextInputLayout nameTextInputLayout;
    @BindView(R.id.find_password_et_name)
    TextInputEditText nameEditText;
    @BindView(R.id.find_password_til_email)
    TextInputLayout emailTextInputLayout;
    @BindView(R.id.find_password_et_email)
    TextInputEditText emailEditText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();

        nameEditText.addTextChangedListener(new BaseTextWatcher(this, nameTextInputLayout, nameEditText, null));
        emailEditText.addTextChangedListener(new BaseTextWatcher(this, emailTextInputLayout, emailEditText, null));
    }

    @OnClick({R.id.find_password_btn_back, R.id.find_password_btn_finish})
    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.find_password_btn_back:
                goToLogin();
                break;
            case R.id.find_password_btn_finish:
                sendEmail();
                break;
        }
    }

    private void goToLogin() {
        final Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToFindPassword2() {
        final Intent intent = new Intent(this,FindPasswordActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void sendEmail() {
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if (checkData(name, email)) return;

        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        goToFindPassword2();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,e.toString());
            }
        });
    }

    private boolean checkData(String name, String email) {
        if (name.isEmpty() || email.isEmpty()) {
            ToastUtils.showToast(this, getString(R.string.login_failure));
            return true;
        } else if (!(name.length() >= 2 && name.length() <= 10)) {
            ToastUtils.showToast(this, getString(R.string.signup_name_err_msg));
            return true;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ToastUtils.showToast(this, getString(R.string.login_email_err_msg));
            return true;
        }
        return false;
    }


}
