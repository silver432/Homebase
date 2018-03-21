package com.jasen.kimjaeseung.homebase.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.data.Team;
import com.jasen.kimjaeseung.homebase.data.User;
import com.jasen.kimjaeseung.homebase.util.BaseTextWatcher;
import com.jasen.kimjaeseung.homebase.util.ProgressUtils;
import com.jasen.kimjaeseung.homebase.util.ToastUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kimjaeseung on 2018. 2. 5..
 */

public class RegisterTeamActivity extends AppCompatActivity {
    private static final String TAG = RegisterTeamActivity.class.getSimpleName();
    @BindView(R.id.register_team_iv_team_logo)
    ImageView teamLogo;
    @BindView(R.id.register_team_fl)
    FrameLayout frameLayout;
    @BindView(R.id.register_team_civ_team_logo)
    CircleImageView circleImageView;
    @BindView(R.id.register_team_til_team_name)
    TextInputLayout nameTextInputLayout;
    @BindView(R.id.register_team_et_team_name)
    TextInputEditText nameEditText;
    @BindView(R.id.register_team_et_team_intro)
    EditText introEditText;
    @BindView(R.id.register_team_tv_intro_err_msg)
    TextView introErrorMsg;

    private int GET_PICTURE_URI = 12;
    private FirebaseDatabase mDataBase;
    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private Uri teamLogoUri;
    private String teamKey;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_team);

        ButterKnife.bind(this);

        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_PICTURE_URI) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    teamLogoUri = data.getData();
                    InputStream inputStream = getContentResolver().openInputStream(teamLogoUri);
                    Drawable mDrawable = Drawable.createFromStream(inputStream, teamLogoUri.toString());
                    teamLogo.setVisibility(View.INVISIBLE);
                    circleImageView.setVisibility(View.VISIBLE);
                    circleImageView.setImageDrawable(mDrawable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void init() {
        nameEditText.addTextChangedListener(new BaseTextWatcher(this, nameTextInputLayout, nameEditText, null));
        introEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String intro = introEditText.getText().toString();

                if (intro.length() > 50) {
                    introErrorMsg.setText(getString(R.string.register_team_intro_err_msg));
                    introErrorMsg.setTextColor(getResources().getColor(R.color.colorRed));
                    introErrorMsg.setVisibility(View.VISIBLE);
                } else {
                    introErrorMsg.setVisibility(View.INVISIBLE);
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();

    }

    @OnClick({R.id.register_team_btn_back, R.id.register_team_fl, R.id.register_team_iv_team_logo, R.id.register_team_btn_confirm})
    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.register_team_btn_back:
                goToSignUp2();
                break;
            case R.id.register_team_fl:
                loadTeamLogo();
                break;
            case R.id.register_team_iv_team_logo:
                loadTeamLogo();
                break;
            case R.id.register_team_btn_confirm:
                registerTeam();
                break;
        }
    }

    private void goToSignUp2() {
        final Intent intent = new Intent(this, SignUpActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void loadTeamLogo() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GET_PICTURE_URI);
    }

    private void registerTeam() {
        String teamName = nameEditText.getText().toString();
        String teamIntro = introEditText.getText().toString();

        if (checkData(teamName, teamIntro)) return;

        ProgressUtils.show(this,R.string.loading);

        String provider = mAuth.getCurrentUser().getProviders().get(0);

        // team db
        DatabaseReference databaseReference = mDataBase.getReference("teams");
        teamKey = databaseReference.push().getKey();
        Team team = new Team(teamName,teamKey+"/teamLogo",teamIntro,null);
        databaseReference.child(teamKey).setValue(team);
        databaseReference.child(teamKey).child("admin").setValue(mAuth.getCurrentUser().getUid());
//        databaseReference.child(teamKey).child("members").push().setValue(mAuth.getCurrentUser().getUid());

        // user db
        DatabaseReference databaseReference1 = mDataBase.getReference("users").child(mAuth.getCurrentUser().getUid());
        if (provider.contains("password")){
            //email
            databaseReference1.child("teamCode").setValue(teamKey);
        }else {
            //google,facebook
            User user = new User(null,null,null,null,null,teamKey);
            databaseReference1.setValue(user);
        }

        StorageReference storageRef = mStorage.getReference(teamKey+"/teamLogo");

        try {
            InputStream inputStream = getContentResolver().openInputStream(teamLogoUri);
            UploadTask uploadTask = storageRef.putStream(inputStream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG,"task success");
                    ProgressUtils.dismiss();
                    goToRegisterTeam2();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"task fail");
                    ProgressUtils.dismiss();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private boolean checkData(String teamName, String teamIntro) {
        if (teamLogo.getVisibility() == View.VISIBLE) {
            ToastUtils.showToast(this, getString(R.string.register_team_logo_err_msg));
            return true;
        } else if (teamName.isEmpty() || teamName.length() > 25) {
            ToastUtils.showToast(this, getString(R.string.register_team_name_err_msg2));
            return true;
        } else if (teamIntro.length() > 50) {
            ToastUtils.showToast(this, getString(R.string.register_team_intro_err_msg2));
            return true;
        }

        return false;
    }

    private void goToRegisterTeam2() {
        final Intent intent = new Intent(this, RegisterTeamActivity2.class);
        intent.putExtra("code",teamKey);
        startActivity(intent);
        finish();
    }

}
