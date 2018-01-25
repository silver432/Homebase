package com.jasen.kimjaeseung.homebase.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.data.Player;
import com.jasen.kimjaeseung.homebase.main.MainActivity;
import com.jasen.kimjaeseung.homebase.network.CloudService;
import com.jasen.kimjaeseung.homebase.util.BaseTextWatcher;
import com.jasen.kimjaeseung.homebase.util.ProgressUtils;
import com.jasen.kimjaeseung.homebase.util.ToastUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kimjaeseung on 2018. 1. 3..
 */

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.login_btn_facebook)
    Button facebookLoginButton;
    @BindView(R.id.login_et_email)
    TextInputEditText signInEmailEditText;
    @BindView(R.id.login_et_password)
    TextInputEditText signInPasswordEditText;
    @BindView(R.id.login_til_email)
    TextInputLayout signInEmailLayout;
    @BindView(R.id.login_til_password)
    TextInputLayout signInPasswordLayout;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private static final int RC_SIGN_IN = 9001;
    public static boolean isRegister = false;

    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void init() {
        initGoogleSignIn();
        initFacebookSignIn();

        //add textwatcher to edittext
        signInEmailEditText.addTextChangedListener(new BaseTextWatcher(this, signInEmailLayout, signInEmailEditText, null));
        signInPasswordEditText.addTextChangedListener(new BaseTextWatcher(this, signInPasswordLayout, signInPasswordEditText, null));

        mAuth = FirebaseAuth.getInstance();
    }

    //Init Google sign in
    private void initGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    //Init Facebook sign in
    private void initFacebookSignIn() {
        mCallbackManager = CallbackManager.Factory.create();
        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                        Arrays.asList("public_profile", "email"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
                    }
                });
            }
        });
    }

    @OnClick({R.id.login_btn_google, R.id.login_tv_sign_up, R.id.login_btn_login, R.id.login_tv_find_email_password})
    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn_google:
                googleSignIn();
                break;
            case R.id.login_tv_sign_up:
                goToSignUp();
                break;
            case R.id.login_btn_login:
                emailSignIn();
                break;
            case R.id.login_tv_find_email_password:
                chooseFindEmailPassword();
                break;
        }
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void goToSignUp() {
        final Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void emailSignIn() {
        String email = signInEmailEditText.getText().toString();
        String password = signInPasswordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            ToastUtils.showToast(this, getString(R.string.login_warning));
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmail:success");

                    checkFirstLogin();
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    ToastUtils.showToast(getApplicationContext(), getString(R.string.login_failure_msg));
                }
            }
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        ProgressUtils.show(this, R.string.loading);

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d(TAG, "signInWithCredential:success");

                            checkFirstLogin();
                        } else {
                            // Sign in fail
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }

                        ProgressUtils.dismiss();
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        ProgressUtils.show(this, R.string.loading);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d(TAG, "signInWithCredential:success");

                            checkFirstLogin();
                        } else {
                            // Sign in fail
                            ToastUtils.showToast(getApplicationContext(), getString(R.string.login_failure_msg));
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }

                        ProgressUtils.dismiss();
                    }
                });
    }

    private void goToRegister() {
        final Intent intent = new Intent(this, SignUpActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    protected void goToMain() {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void checkFirstLogin() {
        mUser = mAuth.getCurrentUser();

        CloudService service = CloudService.retrofit.create(CloudService.class);
        Call<Player> call = service.callPlayer(mUser.getUid());

        call.enqueue(new Callback<Player>() {
            @Override
            public void onResponse(Call<Player> call, Response<Player> response) {
                if (!response.isSuccessful()) {
                    //db에 player 정보 없음
                    goToRegister();
                    isRegister = false;
                    return;
                }
                //player 불러오기 성공
                goToMain();
                isRegister = true;
            }

            @Override
            public void onFailure(Call<Player> call, Throwable t) {
                //네트워크 에러
                Log.d(TAG, "onResponseFailed: " + call.request().url());
            }
        });
    }

    private void chooseFindEmailPassword() {
        final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        final View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_choose_email_password, null);
        alertDialog.setView(view);
        alertDialog.show();

        Button exit = (Button) alertDialog.findViewById(R.id.dialog_choose_btn_exit);
        Button findEmail = (Button) alertDialog.findViewById(R.id.dialog_choose_btn_email);
        Button findPassword = (Button) alertDialog.findViewById(R.id.dialog_choose_btn_password);

        if (exit != null)
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

        if (findEmail != null)
            findEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        if (findEmail != null)
            findEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

    }
}
