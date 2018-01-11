package com.jasen.kimjaeseung.homebase.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.jasen.kimjaeseung.homebase.R;

/**
 * Created by kimjaeseung on 2018. 1. 11..
 */

public class BaseTextWatcher implements TextWatcher {
    private View view;
    private Context context;
    private TextInputLayout textInputLayout;
    private TextInputEditText textInputEditText;
    private TextInputEditText checkInputEditText;

    public BaseTextWatcher(Context context, View view, TextInputEditText textInputEditText, @Nullable TextInputEditText checkEditText) {
        this.context = context;
        this.view = view;
        this.textInputLayout = (TextInputLayout) view;
        this.textInputEditText = textInputEditText;
        this.checkInputEditText=checkEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        switch (view.getId()) {
            case R.id.login_til_email:
                loginValidateEmail();
                break;
            case R.id.signup_til_email:
                signUpValidateEmail();
                break;
            case R.id.signup_til_password_confirm:
                signUpValidatePasswordConfirm();
                break;
        }
    }

    private void loginValidateEmail() {
        String email = textInputEditText.getText().toString();

        if (email.isEmpty() || !isValidEmail(email)) {
            textInputLayout.setError(context.getString(R.string.login_email_err_msg));
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.login_valid_symbol, 0);
            textInputLayout.setErrorEnabled(false);
        }
    }

    private void signUpValidateEmail(){
        String email = textInputEditText.getText().toString();

        if (email.isEmpty() || !isValidEmail(email)) {
            textInputLayout.setError(context.getString(R.string.login_email_err_msg));
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.signup_valid_symbol, 0);
            textInputLayout.setErrorEnabled(false);
        }
    }

    private void signUpValidatePasswordConfirm(){
        String password = checkInputEditText.getText().toString();
        String passwordConfirm = textInputEditText.getText().toString();

        if (!password.equals(passwordConfirm)){
            textInputLayout.setError(context.getString(R.string.signup_password_err_msg));
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }else {
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.signup_valid_symbol, 0);
            textInputLayout.setErrorEnabled(false);
        }
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
