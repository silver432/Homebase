package com.jasen.kimjaeseung.homebase.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.data.PostEmailByName;
import com.jasen.kimjaeseung.homebase.data.Team;
import com.jasen.kimjaeseung.homebase.data.User;
import com.jasen.kimjaeseung.homebase.network.CloudService;
import com.jasen.kimjaeseung.homebase.util.ProgressUtils;
import com.jasen.kimjaeseung.homebase.util.ToastUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kimjaeseung on 2018. 2. 5..
 */

public class EnterTeamActivity extends AppCompatActivity {
    private static final String TAG = EnterTeamActivity.class.getSimpleName();
    @BindView(R.id.enter_team_et_team_code)
    EditText teamCodeEditText;

    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_team);

        ButterKnife.bind(this);

        init();
    }

    private void init(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();

    }

    @OnClick({R.id.enter_team_btn_finish})
    public void mOnClick(View view){
        switch (view.getId()){
            case R.id.enter_team_btn_finish:
                enterTeam();
                break;
        }
    }

    private void enterTeam(){
        ProgressUtils.show(this, R.string.loading);

        final String teamCode = teamCodeEditText.getText().toString();
        if (teamCode.isEmpty()){
            ProgressUtils.dismiss();
            ToastUtils.showToast(this,getString(R.string.please_enter_team_code));
            return;
        }
        //팀코드 존재하는지 확인
        CloudService service = CloudService.retrofit.create(CloudService.class);
        Call<Team> call = service.callTeam(teamCode);
        call.enqueue(new Callback<Team>() {
            @Override
            public void onResponse(Call<Team> call, Response<Team> response) {
                if (!response.isSuccessful()){
                    //팀코드 존재하지 않음
                    ProgressUtils.dismiss();
                    ToastUtils.showToast(EnterTeamActivity.this,getString(R.string.code_not_exist));
                    return;
                }
                //팀코드 존재
                Team mTeam = response.body();
                ProgressUtils.dismiss();

                chooseTeam(teamCode,mTeam);
            }

            @Override
            public void onFailure(Call<Team> call, Throwable t) {
                ProgressUtils.dismiss();

                //네트워크 에러
                Log.d(TAG, "onResponseFailed: " + call.request().url() + " " + t.getMessage());
            }
        });
    }

    private void chooseTeam(final String teamCode,Team team){
        final AlertDialog alertDialog = new AlertDialog.Builder(EnterTeamActivity.this).create();
        final View view = LayoutInflater.from(EnterTeamActivity.this).inflate(R.layout.dialog_choose_team, null);
        alertDialog.setView(view);
        alertDialog.show();

        final CircleImageView ivLogo = (CircleImageView) alertDialog.findViewById(R.id.choose_team_iv_team_logo);
        TextView tvTeam = (TextView)alertDialog.findViewById(R.id.choose_team_tv_team);
        Button confirm = (Button) alertDialog.findViewById(R.id.choose_team_btn_finish);
        Button cancel = (Button) alertDialog.findViewById(R.id.choose_team_btn_cancel);

        if (ivLogo!=null){
            StorageReference storageReference = mStorage.getReference(teamCode+"/teamLogo");
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(EnterTeamActivity.this)
                            .load(uri)
                            .into(ivLogo);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
        }

        if (tvTeam!=null) tvTeam.setText(team.getName());

        if (confirm!=null)
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference databaseReference = mDatabase.getReference("teams").child(teamCode);
                    databaseReference.child("members").push().setValue(mAuth.getCurrentUser().getUid());

                    String provider = mAuth.getCurrentUser().getProviders().get(0);

                    DatabaseReference databaseReference1 = mDatabase.getReference("users").child(mAuth.getCurrentUser().getUid());

                    if (provider.contains("password")){
                        //email
                        databaseReference1.child("teamCode").setValue(teamCode);
                    }else {
                        //google,facebook
                        User user = new User(null,null,null,null,null,teamCode);
                        databaseReference1.setValue(user);
                    }

                    //store teamcode in local
                    SharedPreferences sharedPref = getSharedPreferences("teamPref",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("teamCode",teamCode);
                    editor.apply();

                    goToSignUp3();
                }
            });

        if (cancel!=null)
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
    }

    private void goToSignUp3() {
        final Intent intent = new Intent(this, SignUpActivity3.class);
        startActivity(intent);
        finish();
    }


}
