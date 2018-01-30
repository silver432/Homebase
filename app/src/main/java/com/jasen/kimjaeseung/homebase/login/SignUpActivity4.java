package com.jasen.kimjaeseung.homebase.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.data.Player;
import com.jasen.kimjaeseung.homebase.data.User;
import com.jasen.kimjaeseung.homebase.main.MainActivity;
import com.jasen.kimjaeseung.homebase.util.BaseTextWatcher;
import com.jasen.kimjaeseung.homebase.util.ProgressUtils;
import com.jasen.kimjaeseung.homebase.util.RegularExpressionUtils;
import com.jasen.kimjaeseung.homebase.util.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kimjaeseung on 2018. 1. 15..
 */

public class SignUpActivity4 extends AppCompatActivity {
    private static final String TAG = SignUpActivity4.class.getSimpleName();
    @BindView(R.id.signup4_sp_position)
    Spinner positionSpinner;
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

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup4);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        initSpinner();

        noEditText.addTextChangedListener(new BaseTextWatcher(this, noTextInputLayout, noEditText, null));

    }

    private void initSpinner(){
        String[] arrPosition = getResources().getStringArray(R.array.array_position);
        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this, R.layout.spinner_position,arrPosition);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionSpinner.setAdapter(adapter);
    }

    @OnClick({R.id.signup4_btn_back, R.id.signup4_btn_finish, R.id.signup4_cb_pitcher_left, R.id.signup4_cb_pitcher_right, R.id.signup4_cb_hitter_left, R.id.signup4_cb_hitter_right})
    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.signup4_btn_back:
                goToSignUp3();
                break;
            case R.id.signup4_btn_finish:
                if (checkData()) return;
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

    private boolean checkData() {
        String position = positionSpinner.getSelectedItem().toString();
        String no = noEditText.getText().toString();
        boolean isPitcherChecked = plCheckBox.isChecked() || prCheckBox.isChecked();
        boolean isHitterChecked = hlCheckBox.isChecked() || hrCheckBox.isChecked();

        if (position.isEmpty() || no.isEmpty() || !isPitcherChecked || !isHitterChecked) {
            ToastUtils.showToast(this, getString(R.string.login_failure));
            return true;
        } else if (!no.matches(RegularExpressionUtils.intRegex)) {
            ToastUtils.showToast(this, getString(R.string.signup4_no_err_msg));
            return true;
        }
        return false;
    }

    private void addUserToDB() {

        ProgressUtils.show(this,R.string.loading);

        String name = getIntent().getStringExtra("name");
        String birth = getIntent().getStringExtra("birth");
        String email = mAuth.getCurrentUser().getEmail();
        String provider = mAuth.getCurrentUser().getProviders().get(0);

        String position = positionSpinner.getSelectedItem().toString();
        String no = noEditText.getText().toString();
        String height = getIntent().getStringExtra("height");
        String weight = getIntent().getStringExtra("weight");

        Double dHeight = Double.parseDouble(height);
        Double dWeight = Double.parseDouble(weight);

        String pitcher = plCheckBox.isChecked() ? getString(R.string.left) : getString(R.string.right);
        String hitter = hlCheckBox.isChecked() ? getString(R.string.left) : getString(R.string.right);

        DatabaseReference databaseReference = mDatabase.getReference("users");

        if (provider.contains(getString(R.string.facebook)) || provider.contains(getString(R.string.google))) {
            User user = new User(provider, name, birth, email, null);
            databaseReference.child(mAuth.getCurrentUser().getUid()).setValue(user);
        }

        databaseReference = mDatabase.getReference("players");

        Player player = new Player(name, null, position,Integer.parseInt(no), dHeight, dWeight,hitter,pitcher,new SimpleDateFormat("yyyy.MM.dd").format(new Date()),null );
        databaseReference.child(mAuth.getCurrentUser().getUid()).setValue(player, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                ProgressUtils.dismiss();

                goToMain();
            }
        });

    }

    protected void goToMain() {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
