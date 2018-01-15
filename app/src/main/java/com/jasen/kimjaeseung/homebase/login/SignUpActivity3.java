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

    private void init(){
        mAuth = FirebaseAuth.getInstance();
        //이메일 로그인시 이름과 생년월일 가져오기
//        if (mAuth.getCurrentUser().getProviders().get(0).equals("password")){
//
//        }



        nameEditText.addTextChangedListener(new BaseTextWatcher(this, nameTextInputLayout, nameEditText, null));
        birthEditText.addTextChangedListener(new BaseTextWatcher(this, birthTextInputLayout, birthEditText, null));

    }

    private void setNameAndBirth(){

    }

    @OnClick({R.id.signup3_btn_back,R.id.signup3_btn_next})
    public void mOnClick(View view){
        switch (view.getId()){
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

    private void goToSignUp4(){
        String name = nameEditText.getText().toString();
        String birth = birthEditText.getText().toString();
        String height = heightEditText.getText().toString();
        String weight =weightEditText.getText().toString();

        if (checkEmpty(name,birth,height,weight)) return;

        final Intent intent = new Intent(this, SignUpActivity4.class);
        intent.putExtra("name",name);
        intent.putExtra("birth",birth);
        intent.putExtra("height",height);
        intent.putExtra("weight",weight);
        startActivity(intent);
        finish();
    }

    private boolean checkEmpty(String name,String birth,String height,String weight){
        if (name.isEmpty()||birth.isEmpty()||height.isEmpty()||weight.isEmpty()){
            ToastUtils.showToast(this,getString(R.string.login_failure));
            return true;
        }
        return false;
    }
}
