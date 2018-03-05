package com.jasen.kimjaeseung.homebase.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
        this.checkInputEditText = checkEditText;
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
            case R.id.signup_til_password:
                signUpValidatePassword();
                break;
            case R.id.signup_til_password_confirm:
                signUpValidatePasswordConfirm();
                break;
            case R.id.signup_til_name:
                signUpValidateName();
                break;
            case R.id.signup_til_birth:
                signUpValidateBirth();
                break;
            case R.id.signup3_til_name:
                signUpValidateName();
                break;
            case R.id.signup3_til_birth:
                signUpValidateBirth();
                break;
            case R.id.signup3_til_height:
                signUpValidateHeight();
                break;
            case R.id.signup3_til_weight:
                signUpValidateWeight();
                break;
            case R.id.signup4_til_no:
                signUpValidateNo();
                break;
            case R.id.find_email_til_name:
                signUpValidateName();
                break;
            case R.id.find_email_til_birth:
                signUpValidateBirth();
                break;
            case R.id.find_password_til_name:
                signUpValidateName();
                break;
            case R.id.find_password_til_email:
                signUpValidateEmail();
                break;
            case R.id.register_team_til_team_name:
                registerTeamValidateName();
                break;
        }
    }

    private void loginValidateEmail() {
        String email = textInputEditText.getText().toString();

        if (email.isEmpty() || !isValidEmail(email)) {
            textInputLayout.setError(context.getString(R.string.login_email_err_msg));
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else validate();
    }

    private void signUpValidateEmail() {
        String email = textInputEditText.getText().toString();

        if (email.isEmpty() || !isValidEmail(email)) {
            textInputLayout.setError(context.getString(R.string.login_email_err_msg));
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else validate();

    }

    private void signUpValidatePasswordConfirm() {
        String password = checkInputEditText.getText().toString();
        String passwordConfirm = textInputEditText.getText().toString();

        if (!password.equals(passwordConfirm)) {
            textInputLayout.setError(context.getString(R.string.signup_password_err_msg));
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else validate();

    }

    private void signUpValidateName() {
        String name = textInputEditText.getText().toString();

        if (name.isEmpty() || !isValidateName(name)) {
            textInputLayout.setError(context.getString(R.string.signup_name_err_msg));
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else validate();

    }

    private boolean isValidateName(String name) {
        return name.length() >= 2 && name.length() <= 10;
    }

    private void signUpValidateBirth() {
        String birth = textInputEditText.getText().toString();

        if (birth.matches(RegularExpressionUtils.birth2)){
            birth = birth.replaceAll("(\\d{4})(\\d{2})(\\d{2})","$1.$2.$3");
            textInputEditText.setText(birth);
        }

        if (birth.isEmpty() || !isValidateBirth(birth)) {
            textInputLayout.setError(context.getString(R.string.signup_birth_err_msg));
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            String[] datas = birth.split("\\.");
            int month = Integer.parseInt(datas[1]);
            int day = Integer.parseInt(datas[2]);
            if (month < 1 || month > 12 || day < 1 || day > 31) {
                textInputLayout.setError(context.getString(R.string.signup_birth_err_msg2));
                textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            } else validate();
        }
    }

    private void signUpValidatePassword() {
        String password = textInputEditText.getText().toString();

        if (password.isEmpty() || password.length() < 6) {
            textInputLayout.setError(context.getString(R.string.signup_password_err_msg2));
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else validate();
    }

    private boolean isValidateBirth(String birth) {
        return birth.matches(RegularExpressionUtils.birth);
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void validate() {
        textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.signup_valid_symbol, 0);
        textInputLayout.setErrorEnabled(false);
    }

    private void signUpValidateHeight(){
        String height = textInputEditText.getText().toString();

        if (height.isEmpty()||!height.matches(RegularExpressionUtils.doubleRegex)){
            textInputLayout.setError(context.getString(R.string.please_input_number));
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }else {
            double hDouble = Double.parseDouble(height);
            if (hDouble<0||hDouble>300){
                textInputLayout.setError(context.getString(R.string.signup_height_err_msg));
                textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            } else validate();
        }

    }

    private void signUpValidateWeight(){
        String weight = textInputEditText.getText().toString();

        if (weight.isEmpty()||!weight.matches(RegularExpressionUtils.doubleRegex)){
            textInputLayout.setError(context.getString(R.string.please_input_number));
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }else {
            double wDouble = Double.parseDouble(weight);
            if (wDouble<0||wDouble>300){
                textInputLayout.setError(context.getString(R.string.signup_weight_err_msg));
                textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            } else validate();
        }
    }

    private void signUpValidateNo(){
        String no = textInputEditText.getText().toString();

        if (no.isEmpty()||!no.matches(RegularExpressionUtils.intRegex)){
            textInputLayout.setError(context.getString(R.string.please_input_number));
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }else validate();
    }

    private void registerTeamValidateName(){
        String name = textInputEditText.getText().toString();

        if (name.isEmpty()||name.length()>25){
            textInputLayout.setError(context.getString(R.string.register_team_name_err_msg));
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else validate();

    }
}
