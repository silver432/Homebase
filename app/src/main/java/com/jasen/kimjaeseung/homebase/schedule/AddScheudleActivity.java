package com.jasen.kimjaeseung.homebase.schedule;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.data.Schedule;
import com.jasen.kimjaeseung.homebase.util.BaseTextWatcher;
import com.jasen.kimjaeseung.homebase.util.SharedPrefUtils;
import com.jasen.kimjaeseung.homebase.util.ToastUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kimjaeseung on 2018. 3. 10..
 */

public class AddScheudleActivity extends AppCompatActivity {
    private static final String TAG = AddScheudleActivity.class.getSimpleName();

    private FirebaseDatabase mDatabase;

    @BindView(R.id.add_schedule_til_opponent)
    TextInputLayout tilOpponent;
    @BindView(R.id.add_schedule_til_place)
    TextInputLayout tilPlace;
    @BindView(R.id.add_schedule_et_opponent)
    TextInputEditText etOpponent;
    @BindView(R.id.add_schedule_et_place)
    TextInputEditText etPlace;
    @BindView(R.id.add_schedule_tv_date)
    TextView tvDate;
    @BindView(R.id.add_schedule_tv_time)
    TextView tvTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        mDatabase = FirebaseDatabase.getInstance();

        etOpponent.addTextChangedListener(new BaseTextWatcher(this, tilOpponent, etOpponent, null));
        etPlace.addTextChangedListener(new BaseTextWatcher(this,tilPlace,etPlace,null));

    }

    @OnClick({R.id.add_schedule_tv_confirm,R.id.add_schedule_btn_back,R.id.add_schedule_tv_time,R.id.add_schedule_tv_date})
    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.add_schedule_tv_confirm:
                addSchedule();
                break;
            case R.id.add_schedule_btn_back:
                finish();
                break;
            case R.id.add_schedule_tv_date:
                openDatePicker();
                break;
            case R.id.add_schedule_tv_time:
                openTimePicker();
                break;

        }
    }

    private void addSchedule() {
        String opponent = etOpponent.getText().toString();
        String stadium = etPlace.getText().toString();
        String date = tvDate.getText().toString();
        String time = tvTime.getText().toString();

        //check data
        if (opponent.isEmpty()||stadium.isEmpty()||date.equals(getString(R.string.date))||time.equals(getString(R.string.time))){
            ToastUtils.showToast(this,getString(R.string.fill_blank));
            return;
        }

        //get teamcode in local
        String teamCode = SharedPrefUtils.getTeamCode(this);

        DatabaseReference databaseReference = mDatabase.getReference("schedules");
        Schedule schedule = new Schedule(date+" "+time+":00", stadium, opponent,String.valueOf(-1),String.valueOf(-1));
        databaseReference.child(teamCode).push().setValue(schedule);

        finish();
    }

    private void openDatePicker(){
        GregorianCalendar calendar = new GregorianCalendar();
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisDay = calendar.get(Calendar.DAY_OF_WEEK);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String date = i+"-"+(i1+1)+"-"+i2;
                tvDate.setText(date);
                tvDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.signup_valid_symbol, 0);
            }
        },thisYear,thisMonth,thisDay).show();
    }

    private void openTimePicker(){
        GregorianCalendar calendar = new GregorianCalendar();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                String time = i+":"+i1;
                tvTime.setText(time);
                tvTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.signup_valid_symbol, 0);
            }
        },hour, minute,false).show();

    }
}
