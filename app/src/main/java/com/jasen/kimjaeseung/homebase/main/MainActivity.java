package com.jasen.kimjaeseung.homebase.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.data.Player;
import com.jasen.kimjaeseung.homebase.login.LoginActivity;
import com.jasen.kimjaeseung.homebase.login.SignUpActivity2;
import com.jasen.kimjaeseung.homebase.network.CloudService;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Log.d(TAG, "onCreate");

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.main_btn_sign_out})
    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.main_btn_sign_out:
                signOut();
                break;
        }
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "signed in : " + user.getUid());

                    //이후 global app으로이동
                    checkRegister(user);

                } else {
                    goToLogin();
                }
            }
        };
    }

    protected void goToLogin() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void goToRegister() {
        final Intent intent = new Intent(this, SignUpActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void checkRegister(FirebaseUser mUser) {
        CloudService service = CloudService.retrofit.create(CloudService.class);
        Call<Player> call = service.callPlayer(mUser.getUid());

        call.enqueue(new Callback<Player>() {
            @Override
            public void onResponse(Call<Player> call, Response<Player> response) {
                if (!response.isSuccessful()) {
                    //db에 player 정보 없음
                    goToRegister();
                    return;
                }
                //player 불러오기 성공

            }

            @Override
            public void onFailure(Call<Player> call, Throwable t) {
                //네트워크 에러
                Log.d(TAG, "onResponseFailed: " + call.request().url());
            }
        });

    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        LoginManager.getInstance().logOut();
    }
}
