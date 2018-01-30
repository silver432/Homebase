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

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.data.User;
import com.jasen.kimjaeseung.homebase.main.MainActivity;
import com.jasen.kimjaeseung.homebase.util.BaseTextWatcher;
import com.jasen.kimjaeseung.homebase.util.ProgressUtils;
import com.jasen.kimjaeseung.homebase.util.RegularExpressionUtils;
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
    @BindView(R.id.signup_til_name)
    TextInputLayout nameTextInputLayout;
    @BindView(R.id.signup_et_name)
    TextInputEditText nameEditText;
    @BindView(R.id.signup_til_birth)
    TextInputLayout birthTextInputLayout;
    @BindView(R.id.signup_et_birth)
    TextInputEditText birthEditText;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        emailEditText.addTextChangedListener(new BaseTextWatcher(this, emailTextInputLayout, emailEditText, null));
        passwordEditText.addTextChangedListener(new BaseTextWatcher(this, passwordTextInputLayout, passwordEditText, null));
        passwordConfirmEditText.addTextChangedListener(new BaseTextWatcher(this, passwordTextInputLayoutConfirm, passwordConfirmEditText, passwordEditText));
        nameEditText.addTextChangedListener(new BaseTextWatcher(this, nameTextInputLayout, nameEditText, null));
        birthEditText.addTextChangedListener(new BaseTextWatcher(this, birthTextInputLayout, birthEditText, null));

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
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
        final String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordConfirm = passwordConfirmEditText.getText().toString();
        final String name = nameEditText.getText().toString();
        final String birth = birthEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty() || name.isEmpty() || birth.isEmpty()) {
            ToastUtils.showToast(this, getString(R.string.login_failure));
            return;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ToastUtils.showToast(this, getString(R.string.login_email_err_msg));
            return;
        } else if (password.length() < 6) {
            ToastUtils.showToast(this, getString(R.string.signup_password_err_msg2));
            return;
        } else if (!password.equals(passwordConfirm)) {
            ToastUtils.showToast(this, getString(R.string.signup_password_err_msg));
            return;
        } else if (!(name.length() >= 2 && name.length() <= 10)) {
            ToastUtils.showToast(this, getString(R.string.signup_name_err_msg));
            return;
        } else if (!birth.matches(RegularExpressionUtils.birth)) {
            ToastUtils.showToast(this, getString(R.string.signup_birth_err_msg));
            return;
        } else if(birth.matches(RegularExpressionUtils.birth)){
            String[] datas = birth.split("\\.");
            int month = Integer.parseInt(datas[1]);
            int day = Integer.parseInt(datas[2]);
            if (month<1||month>12||day<1||day>31){
                ToastUtils.showToast(this, getString(R.string.signup_birth_err_msg2));
                return;
            }
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    ToastUtils.showToast(getApplicationContext(), getString(R.string.signup_success));

                    addUserToDB(name, email, birth);

                    goToLogin();
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException e) {
                        ToastUtils.showToast(getApplicationContext(), getApplicationContext().getString(R.string.signup_user_collision));
                    } catch (Exception e) {
                        ToastUtils.showToast(getApplicationContext(), getString(R.string.signup_failure));
                    }
                }
            }
        });
    }

    private void addUserToDB(String name, String email, String birth) {
        ProgressUtils.show(this,R.string.loading);

        String provider = mAuth.getCurrentUser().getProviders().get(0);
        User user = new User(provider, name, birth, email, null);

        DatabaseReference databaseReference = mDatabase.getReference("users");
        databaseReference.child(mAuth.getCurrentUser().getUid()).setValue(user);

        ProgressUtils.dismiss();
    }

    protected void goToLogin() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
