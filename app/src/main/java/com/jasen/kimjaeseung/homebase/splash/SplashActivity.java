package com.jasen.kimjaeseung.homebase.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jasen.kimjaeseung.homebase.data.Player;
import com.jasen.kimjaeseung.homebase.data.User;
import com.jasen.kimjaeseung.homebase.login.LoginActivity;
import com.jasen.kimjaeseung.homebase.login.SignUpActivity2;
import com.jasen.kimjaeseung.homebase.login.SignUpActivity3;
import com.jasen.kimjaeseung.homebase.main.MainActivity;
import com.jasen.kimjaeseung.homebase.network.CloudService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kimjaeseung on 2018. 1. 6..
 */

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    private void init(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "signed in : " + user.getUid());

                    checkRegister(user);

                } else {
                    goToLogin();
                }
            }
        };
    }

    private void checkRegister(final FirebaseUser mUser) {
        final CloudService service = CloudService.retrofit.create(CloudService.class);

        Call<User> call = service.callUser(mUser.getUid());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    //db에 user 정보 없음 firebase,google인 경우
                    goToMakeTeam();
                    return;
                }
                //user 불러오기 성공
                User user = response.body();
                if (!user.getTeamCode().equals("default")) {
                    //유저가 팀에 가입된 경우
                    CloudService service2 = CloudService.retrofit.create(CloudService.class);
                    Call<Player> call2 = service2.callPlayer(mUser.getUid());
                    call2.enqueue(new Callback<Player>() {
                        @Override
                        public void onResponse(Call<Player> call, Response<Player> response) {
                            if (!response.isSuccessful()) {
                                //db에 player 정보 없음
                                goToSignUp3();
                                return;
                            }
                            //player 불러오기 성공
                            goToMain();
                        }

                        @Override
                        public void onFailure(Call<Player> call, Throwable t) {
                            //네트워크 에러
                            Log.d(TAG, "onResponseFailed: " + call.request().url());
                        }
                    });
                }
                else goToMakeTeam();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //네트워크 에러
                Log.d(TAG, "onResponseFailed: " + call.request().url());
            }
        });

    }

    protected void goToLogin() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void goToMakeTeam() {
        final Intent intent = new Intent(this, SignUpActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void goToSignUp3() {
        final Intent intent = new Intent(this, SignUpActivity3.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    protected void goToMain() {
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
