package com.jasen.kimjaeseung.homebase.login;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.data.Team;
import com.jasen.kimjaeseung.homebase.main.MainActivity;
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

public class RegisterTeamActivity2 extends AppCompatActivity {
    private static final String TAG = RegisterTeamActivity2.class.getSimpleName();

    @BindView(R.id.register_team2_tv_team_code)
    TextView teamCodeTextView;
    @BindView(R.id.register_team2_iv_logo)
    CircleImageView logoImageView;

    private FirebaseStorage mStorage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_team2);

        ButterKnife.bind(this);

        init();
    }

    private void init(){
        mStorage = FirebaseStorage.getInstance();

        final String teamCode = getIntent().getStringExtra("code");
        teamCodeTextView.setText(teamCode);

        //get Team
        CloudService service = CloudService.retrofit.create(CloudService.class);
        Call<Team> call = service.callTeam(teamCode);
        call.enqueue(new Callback<Team>() {
            @Override
            public void onResponse(Call<Team> call, Response<Team> response) {
                if (!response.isSuccessful()){
                    //팀코드 존재하지 않음
                    ProgressUtils.dismiss();
                    return;
                }
                //팀코드 존재
                Team mTeam = response.body();

                StorageReference storageReference = mStorage.getReference(teamCode+"/teamLogo");
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(RegisterTeamActivity2.this)
                                .load(uri)
                                .into(logoImageView);
                        ProgressUtils.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        ProgressUtils.dismiss();
                    }
                });

                //store teamcode in local
                SharedPreferences sharedPref = getSharedPreferences("teamPref",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("teamCode",teamCode);
                editor.apply();

            }

            @Override
            public void onFailure(Call<Team> call, Throwable t) {
                ProgressUtils.dismiss();

                //네트워크 에러
                Log.d(TAG, "onResponseFailed: " + call.request().url() + " " + t.getMessage());
            }
        });

    }

    @OnClick({R.id.register_team2_btn_finish,R.id.register_team2_btn_copy})
    public void mOnClick(View view){
        switch (view.getId()){
            case R.id.register_team2_btn_finish:
               goToSignUp3();
                break;
            case R.id.register_team2_btn_copy:
                copyCode();
                break;
        }
    }

    private void copyCode(){
        String teamCode = getIntent().getStringExtra("code");
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("team_code",teamCode );
        clipboard.setPrimaryClip(clip);

        ToastUtils.showToast(this,getString(R.string.copy_complete));
    }

    private void goToSignUp3() {
        final Intent intent = new Intent(this, SignUpActivity3.class);
        startActivity(intent);
        finish();
    }
}
