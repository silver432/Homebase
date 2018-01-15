package com.jasen.kimjaeseung.homebase.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.data.Player;
import com.jasen.kimjaeseung.homebase.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kimjaeseung on 2018. 1. 15..
 */

public class SignUpActivity4 extends AppCompatActivity {
    private static final String TAG = SignUpActivity4.class.getSimpleName();
    @BindView(R.id.signup4_til_position)
    TextInputLayout positionTextInputLayout;
    @BindView(R.id.signup4_et_position)
    TextInputEditText positionEditText;
    @BindView(R.id.signup4_til_no)
    TextInputLayout noTextInputLayout;
    @BindView(R.id.signup4_et_no)
    TextInputEditText noEditText;
    @BindView(R.id.signup4_cb_pitcher_left)
    CheckBox plCheckBox;
    @BindView(R.id.signup4_cb_pitcher_right)
    CheckBox prCheckBox;
    @BindView(R.id.signup4_cb_hitter_left)
    CheckBox hlCheckBox;
    @BindView(R.id.signup4_cb_hitter_right)
    CheckBox hrCheckBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup4);

        ButterKnife.bind(this);

        init();
    }

    private void init(){

    }

    @OnClick({R.id.signup4_btn_back,R.id.signup4_btn_finish,R.id.signup4_cb_pitcher_left,R.id.signup4_cb_pitcher_right,R.id.signup4_cb_hitter_left,R.id.signup4_cb_hitter_right})
    public void mOnClick(View view){
        switch (view.getId()){
            case R.id.signup4_btn_back:
                goToSignUp3();
                break;
            case R.id.signup4_btn_finish:
                if (checkEmpty()) return;

                addUserToDB();
                break;
            case R.id.signup4_cb_pitcher_left:
                if (prCheckBox.isChecked()) prCheckBox.setChecked(false);
                break;
            case R.id.signup4_cb_pitcher_right:
                if (plCheckBox.isChecked()) plCheckBox.setChecked(false);
                break;
            case R.id.signup4_cb_hitter_left:
                if (hrCheckBox.isChecked()) hrCheckBox.setChecked(false);
                break;
            case R.id.signup4_cb_hitter_right:
                if (hlCheckBox.isChecked()) hlCheckBox.setChecked(false);
                break;
        }
    }

    private void goToSignUp3() {
        final Intent intent = new Intent(this, SignUpActivity3.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private boolean checkEmpty(){
        String position = positionEditText.getText().toString();
        String no = noEditText.getText().toString();
        boolean isPitcherChecked = plCheckBox.isChecked()||prCheckBox.isChecked();
        boolean isHitterChecked = hlCheckBox.isChecked()||hrCheckBox.isChecked();

        if (position.isEmpty()||no.isEmpty()||!isPitcherChecked||!isHitterChecked){
            ToastUtils.showToast(this,getString(R.string.login_failure));
            return true;
        }
        return false;
    }

    private void addUserToDB(){
//        String name = getIntent().getStringExtra("name");
//        String birth = getIntent().getStringExtra("birth");
//        int backNumber = Integer.parseInt(noEditText.getText().toString());
//        String batPosition =
//
//        Player player = new Player(name,null,)
    }
}
