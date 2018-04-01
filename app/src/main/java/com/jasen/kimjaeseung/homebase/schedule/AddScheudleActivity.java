package com.jasen.kimjaeseung.homebase.schedule;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.data.Schedule;
import com.jasen.kimjaeseung.homebase.util.SharedPrefUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @BindView(R.id.add_schedule_dp)
    DatePicker datePicker;
    @BindView(R.id.add_schedule_tp)
    TimePicker timePicker;
    @BindView(R.id.add_schedule_et_place)
    EditText placeEditText;
    @BindView(R.id.add_schedule_et_opponent)
    EditText opponentEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        mDatabase = FirebaseDatabase.getInstance();

    }

    @OnClick({R.id.add_schedule_btn_confirm})
    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.add_schedule_btn_confirm:
                addSchedule();
                break;
        }
    }

    private void addSchedule() {
        //get teamcode in local
        String teamCode = SharedPrefUtils.getTeamCode(this);

        DatabaseReference databaseReference = mDatabase.getReference("schedules");

        int year = datePicker.getYear();
        int month = datePicker.getMonth() + 1;
        int day = datePicker.getDayOfMonth();

        String date = year + "-" + month + "-" + day;

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        Date dateResult = null;
        try {
            dateResult = sdf.parse(date + " " + hour + ":" + minute + ":00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String place = placeEditText.getText().toString();
        String opponent = opponentEditText.getText().toString();

        Schedule schedule = new Schedule(sdf.format(dateResult), place, opponent,String.valueOf(-1),String.valueOf(-1));

        databaseReference.child(teamCode).push().setValue(schedule);

        finish();
    }
}
