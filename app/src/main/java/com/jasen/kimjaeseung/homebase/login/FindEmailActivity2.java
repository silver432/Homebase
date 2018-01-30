package com.jasen.kimjaeseung.homebase.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jasen.kimjaeseung.homebase.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kimjaeseung on 2018. 1. 26..
 */

public class FindEmailActivity2 extends AppCompatActivity {
    private static final String TAG = FindEmailActivity2.class.getSimpleName();

    @BindView(R.id.find_email2_show_email2)
    TextView emailTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_email2);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        initView();

    }

    private void initView() {
        String allEmails = "";
        String emails[] = getIntent().getStringArrayExtra("emails");
        for (String email : emails) {
            allEmails += email + "\n";
        }
        emailTextView.setText(allEmails);
    }

    @OnClick({R.id.find_email2_btn_goto_login, R.id.find_email2_btn_find_password})
    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.find_email2_btn_goto_login:
                goToLogin();
                break;
            case R.id.find_email2_btn_find_password:
                goToFindPassword();
                break;
        }
    }

    protected void goToLogin() {
        final Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    protected void goToFindPassword() {
        final Intent intent = new Intent(this, FindPasswordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
