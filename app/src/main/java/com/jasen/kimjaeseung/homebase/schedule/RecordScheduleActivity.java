package com.jasen.kimjaeseung.homebase.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.data.Player;
import com.jasen.kimjaeseung.homebase.data.Schedule;
import com.jasen.kimjaeseung.homebase.data.Team;
import com.jasen.kimjaeseung.homebase.network.CloudService;
import com.jasen.kimjaeseung.homebase.util.ProgressUtils;
import com.jasen.kimjaeseung.homebase.util.ToastUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kimjaeseung on 2018. 3. 14..
 */

public class RecordScheduleActivity extends AppCompatActivity {
    private static final String TAG = RecordScheduleActivity.class.getSimpleName();

    @BindView(R.id.record_schedule_btn_back)
    Button btnBack;
    @BindView(R.id.record_schedule_tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.record_schedule_tv_opponent)
    TextView tvOpponent;
    @BindView(R.id.record_schedule_tv_when)
    TextView tvWhen;
    @BindView(R.id.record_schedule_tv_where)
    TextView tvWhere;
    @BindView(R.id.record_schedule_rv)
    RecyclerView recyclerView;

    private Schedule schedule;
    private String teamCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_schedule);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        //get data
        schedule = (Schedule) getIntent().getSerializableExtra("schedule");
        teamCode = getIntent().getStringExtra("teamCode");

        //init view
        tvOpponent.setText(schedule.getOpponentTeam());
        tvWhen.setText(schedule.getMatchDate());
        tvWhere.setText(schedule.getMatchPlace());

        initRecyclerView();

    }

    @OnClick({R.id.record_schedule_btn_back, R.id.record_schedule_tv_confirm})
    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.record_schedule_btn_back:
                finish();
                break;
            case R.id.record_schedule_tv_confirm:

                break;
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        MemberAdapter memberAdapter = new MemberAdapter(this);
//        memberAdapter.setItemList(getPlayers());

        recyclerView.setAdapter(memberAdapter);
    }

//    private List<Player> getPlayers() {
//        ProgressUtils.show(this, R.string.loading);
//
//        //팀 불러오기
//        final List<String> memberIds = new ArrayList<>();
//        CloudService service = CloudService.retrofit.create(CloudService.class);
//        Call<Team> call = service.callTeam(teamCode);
//        call.enqueue(new Callback<Team>() {
//            @Override
//            public void onResponse(Call<Team> call, Response<Team> response) {
//                if (!response.isSuccessful()) {
//                    //team 불러오기실패
//                    ProgressUtils.dismiss();
//                    Log.d(TAG,response.body()+" "+teamCode);
//                    ToastUtils.showToast(getApplicationContext(), getString(R.string.loading_error));
//                    return;
//                }
//                //team 존재
//                Team team = response.body();
//                if (team != null) memberIds.addAll(team.getMembers());
//            }
//
//            @Override
//            public void onFailure(Call<Team> call, Throwable t) {
//                ProgressUtils.dismiss();
//
//                //네트워크 에러
//                Log.d(TAG, "onResponseFailed: " + call.request().url() + " " + t.getMessage());
//            }
//        });
//
//        //멤버 불러오기
//        final List<Player> players = new ArrayList<>();
//        for (String id : memberIds) {
//            CloudService service1 = CloudService.retrofit.create(CloudService.class);
//            Call<Player> call1 = service1.callPlayer(id);
//            call1.enqueue(new Callback<Player>() {
//                @Override
//                public void onResponse(Call<Player> call, Response<Player> response) {
//                    if (!response.isSuccessful()){
//                        ProgressUtils.dismiss();
//                        ToastUtils.showToast(getApplicationContext(),getString(R.string.loading_error));
//                        Log.d(TAG,"player");
//                        return;
//                    }
//                    Player player = response.body();
//                    players.add(player);
//                }
//
//                @Override
//                public void onFailure(Call<Player> call, Throwable t) {
//                    ProgressUtils.dismiss();
//
//                    //네트워크 에러
//                    Log.d(TAG, "onResponseFailed: " + call.request().url() + " " + t.getMessage());
//                }
//            });
//        }
//        ProgressUtils.dismiss();
//
//        return players;
//    }
}
