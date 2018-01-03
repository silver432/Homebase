package com.jasen.kimjaeseung.homebase.login;

import android.app.Activity;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kimjaeseung on 2018. 1. 3..
 */

public class LoginPresenterImpl implements LoginPresenter.Presenter {
    private LoginPresenter.View view;
    private Activity mActivity;
    private LoginModel loginModel;

    private static final int RC_SIGN_IN = 123;

    @Override
    public void attachView(LoginPresenter.View view, Activity activity) {
        this.view = view;

        mActivity = activity;

        loginModel = new LoginModel();

        loginModel.setOnAuthLisenter(firebaseAuthCallback);
        loginModel.setActivity(activity);
        loginModel.loadAuth();
    }

    @Override
    public void detachView() {
        view = null;
        mActivity = null;

        loginModel.setActivity(null);
        loginModel.setOnAuthLisenter(null);

        firebaseAuthCallback = null;
    }

    @Override
    public void firebaseLogin() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());

        mActivity.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

    }


    //auth 존재하면 main으로 이동
    private LoginModel.FirebaseAuthCallback firebaseAuthCallback = new LoginModel.FirebaseAuthCallback() {
        @Override
        public void onExist() {
            view.goToMain();
        }

        @Override
        public void onNotExist() {
            firebaseLogin();
        }
    };
}
