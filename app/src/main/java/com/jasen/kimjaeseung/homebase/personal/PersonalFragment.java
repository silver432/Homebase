package com.jasen.kimjaeseung.homebase.personal;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.login.LoginActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kimjaeseung on 2018. 3. 6..
 */

public class PersonalFragment extends Fragment {
    private static final String TAG = PersonalFragment.class.getSimpleName();
    private FirebaseAuth mAuth;

    public PersonalFragment(){}

    public static Fragment getInstance(){
        return new PersonalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this,view);

        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    @OnClick({R.id.personal_btn_signout})
    public void mOnClick(View view){
        switch (view.getId()){
            case R.id.personal_btn_signout:
               signOut();
                break;
        }
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        LoginManager.getInstance().logOut();

        goToLogin();
    }

    protected void goToLogin() {
        final Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
