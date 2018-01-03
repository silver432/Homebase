package com.jasen.kimjaeseung.homebase.login;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by kimjaeseung on 2018. 1. 3..
 */

public class LoginModel {
    private static final String TAG = LoginModel.class.getSimpleName();

    private Activity mActivity;

    private FirebaseAuth mAuth;

    private FirebaseAuthCallback firebaseAuthCallback;

    public interface FirebaseAuthCallback{
        void onExist();
        void onNotExist();
    }

    public void setActivity(Activity activity){
        mActivity = activity;
    }

    //콜백등록
    public void setOnAuthLisenter(FirebaseAuthCallback callback){
        firebaseAuthCallback = callback;
    }

    //load auth
    public void loadAuth(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null){
            firebaseAuthCallback.onExist();
        }else {
            firebaseAuthCallback.onNotExist();
        }
    }

}
