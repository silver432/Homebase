package com.jasen.kimjaeseung.homebase.login;

import android.app.Activity;

/**
 * Created by kimjaeseung on 2018. 1. 3..
 */

public interface LoginPresenter {
    interface View {
        void goToMain();
    }

    interface Presenter {
        void attachView(View view, Activity activity);

        void detachView();

        void firebaseLogin();
    }
}
