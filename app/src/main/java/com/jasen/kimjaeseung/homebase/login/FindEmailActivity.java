package com.jasen.kimjaeseung.homebase.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.data.PostRequestEmail;
import com.jasen.kimjaeseung.homebase.network.CloudService;
import com.jasen.kimjaeseung.homebase.util.BaseTextWatcher;
import com.jasen.kimjaeseung.homebase.util.RegularExpressionUtils;
import com.jasen.kimjaeseung.homebase.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kimjaeseung on 2018. 1. 26..
 */

public class FindEmailActivity extends AppCompatActivity {
    private static final String TAG = FindEmailActivity.class.getSimpleName();

    @BindView(R.id.find_email_til_name)
    TextInputLayout nameTextInputLayout;
    @BindView(R.id.find_email_et_name)
    TextInputEditText nameEditText;
    @BindView(R.id.find_email_til_birth)
    TextInputLayout birthTextInputLayout;
    @BindView(R.id.find_email_et_birth)
    TextInputEditText birthEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_email);

        ButterKnife.bind(this);

        init();
    }

    private void init() {

        nameEditText.addTextChangedListener(new BaseTextWatcher(this, nameTextInputLayout, nameEditText, null));
        birthEditText.addTextChangedListener(new BaseTextWatcher(this, birthTextInputLayout, birthEditText, null));
    }

    @OnClick({R.id.find_email_btn_back, R.id.find_email_btn_finish})
    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.find_email_btn_back:
                goToLogin();
                break;
            case R.id.find_email_btn_finish:
                goToFindEmail2();
                break;

        }
    }

    private void goToLogin() {
        final Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToFindEmail2() {
        String name = nameEditText.getText().toString();
        String birth = birthEditText.getText().toString();

        if (checkData(name, birth)) return;

        CloudService service = CloudService.retrofit.create(CloudService.class);
        Call<String> call = service.findEmail(new PostRequestEmail(name,birth));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()){
                    Log.d(TAG,"retrieve fail");
                    ToastUtils.showToast(FindEmailActivity.this,getString(R.string.find_emaill_fail));
                    return;
                }

                String mEmail = response.body();

                final Intent intent = new Intent(FindEmailActivity.this, FindEmailActivity2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("email",mEmail);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //네트워크 에러
                Log.d(TAG, "onResponseFailed: " + call.request().url()+" "+t.getMessage());
            }
        });

    }

    private boolean checkData(String name, String birth) {
        if (name.isEmpty() || birth.isEmpty()) {
            ToastUtils.showToast(this, getString(R.string.login_failure));
            return true;
        } else if (!(name.length() >= 2 && name.length() <= 10)) {
            ToastUtils.showToast(this, getString(R.string.signup_name_err_msg));
            return true;
        } else if (!birth.matches(RegularExpressionUtils.birth)) {
            ToastUtils.showToast(this, getString(R.string.signup_birth_err_msg));
            return true;
        } else if (birth.matches(RegularExpressionUtils.birth)) {
            String[] datas = birth.split("\\.");
            int month = Integer.parseInt(datas[1]);
            int day = Integer.parseInt(datas[2]);
            if (month < 1 || month > 12 || day < 1 || day > 31) {
                ToastUtils.showToast(this, getString(R.string.signup_birth_err_msg2));
                return true;
            }
        }
        return false;
    }


}
