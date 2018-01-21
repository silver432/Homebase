package com.jasen.kimjaeseung.homebase.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.util.BaseTextWatcher;
import com.jasen.kimjaeseung.homebase.util.RegularExpressionUtils;
import com.jasen.kimjaeseung.homebase.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kimjaeseung on 2018. 1. 15..
 */

public class SignUpActivity3 extends AppCompatActivity {
    private static final String TAG = SignUpActivity3.class.getSimpleName();

    @BindView(R.id.signup3_til_name)
    TextInputLayout nameTextInputLayout;
    @BindView(R.id.signup3_et_name)
    TextInputEditText nameEditText;
    @BindView(R.id.signup3_til_birth)
    TextInputLayout birthTextInputLayout;
    @BindView(R.id.signup3_et_birth)
    TextInputEditText birthEditText;
    @BindView(R.id.signup3_til_height)
    TextInputLayout heightTextInputLayout;
    @BindView(R.id.signup3_et_height)
    TextInputEditText heightEditText;
    @BindView(R.id.signup3_til_weight)
    TextInputLayout weightTextInputLayout;
    @BindView(R.id.signup3_et_weight)
    TextInputEditText weightEditText;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();


        //이메일 로그인시 이름과 생년월일 가져오기


        nameEditText.addTextChangedListener(new BaseTextWatcher(this, nameTextInputLayout, nameEditText, null));
        birthEditText.addTextChangedListener(new BaseTextWatcher(this, birthTextInputLayout, birthEditText, null));
        heightEditText.addTextChangedListener(new BaseTextWatcher(this, heightTextInputLayout, heightEditText, null));
        weightEditText.addTextChangedListener(new BaseTextWatcher(this, weightTextInputLayout, weightEditText, null));

    }

    @OnClick({R.id.signup3_btn_back, R.id.signup3_btn_next})
    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.signup3_btn_back:
                goToSignUp2();
                break;
            case R.id.signup3_btn_next:
                goToSignUp4();
                break;
        }
    }

    private void goToSignUp2() {
        final Intent intent = new Intent(this, SignUpActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void goToSignUp4() {
        String name = nameEditText.getText().toString();
        String birth = birthEditText.getText().toString();
        String height = heightEditText.getText().toString();
        String weight = weightEditText.getText().toString();

        if (checkData(name, birth, height, weight)) return;

        final Intent intent = new Intent(this, SignUpActivity4.class);
        intent.putExtra("name", name);
        intent.putExtra("birth", birth);
        intent.putExtra("height", height);
        intent.putExtra("weight", weight);
        startActivity(intent);
        finish();
    }

    private boolean checkData(String name, String birth, String height, String weight) {
        if (name.isEmpty() || birth.isEmpty() || height.isEmpty() || weight.isEmpty()) {
            ToastUtils.showToast(this, getString(R.string.login_failure));
            return true;
        } else if (!(name.length() >= 2 && name.length() <= 10)) {
            ToastUtils.showToast(this, getString(R.string.signup_name_err_msg));
            return true;
        } else if (!birth.matches(RegularExpressionUtils.birth)) {
            ToastUtils.showToast(this, getString(R.string.signup_birth_err_msg));
            return true;
        } else if (birth.matches(RegularExpressionUtils.birth)) {
            String[] datas = birth.split("\\.");
            int month = Integer.parseInt(datas[1]);
            int day = Integer.parseInt(datas[2]);
            if (month < 1 || month > 12 || day < 1 || day > 31) {
                ToastUtils.showToast(this, getString(R.string.signup_birth_err_msg2));
                return true;
            }
        } else if (!height.matches(RegularExpressionUtils.doubleRegex)) {
            ToastUtils.showToast(this, getString(R.string.signup_height_err_msg));
            return true;
        } else if (height.matches(RegularExpressionUtils.doubleRegex)) {
            double hDouble = Double.parseDouble(height);
            if (hDouble < 0 || hDouble > 300) {
                ToastUtils.showToast(this, getString(R.string.signup_height_err_msg));
                return true;
            }
        } else if (!weight.matches(RegularExpressionUtils.doubleRegex)) {
            ToastUtils.showToast(this, getString(R.string.signup_weight_err_msg));
            return true;
        } else if (weight.matches(RegularExpressionUtils.doubleRegex)) {
            double wDouble = Double.parseDouble(weight);
            if (wDouble < 0 || wDouble > 300) {
                ToastUtils.showToast(this, getString(R.string.signup_weight_err_msg));
                return true;
            }
        }

        return false;
    }
}
