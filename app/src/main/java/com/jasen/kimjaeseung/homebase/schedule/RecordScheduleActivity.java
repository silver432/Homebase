package com.jasen.kimjaeseung.homebase.schedule;

import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.data.Player;
import com.jasen.kimjaeseung.homebase.data.Schedule;
import com.jasen.kimjaeseung.homebase.data.Team;
import com.jasen.kimjaeseung.homebase.network.CloudService;
import com.jasen.kimjaeseung.homebase.util.ProgressUtils;
import com.jasen.kimjaeseung.homebase.util.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jasen.kimjaeseung.homebase.schedule.MemberAdapter.records;

/**
 * Created by kimjaeseung on 2018. 3. 14..
 */

public class RecordScheduleActivity extends AppCompatActivity {
    private static final String TAG = RecordScheduleActivity.class.getSimpleName();

    @BindView(R.id.record_schedule_tv_record)
    TextView tvRecord;
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
    @BindView(R.id.record_schedule_tv_homescore)
    TextView tvHomeScore;
    @BindView(R.id.record_schedule_tv_opponentscore)
    TextView tvOpponentScore;

    private Schedule schedule;
    private String teamCode;
    private boolean isChange;
    private FirebaseDatabase mDatabase;
    List<Player> players = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_schedule);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        mDatabase = FirebaseDatabase.getInstance();

        //get data
        schedule = (Schedule) getIntent().getSerializableExtra("schedule");
        teamCode = getIntent().getStringExtra("teamCode");
        isChange = getIntent().getBooleanExtra("ischange",false);

        //init view
        if (isChange) tvRecord.setText(this.getString(R.string.change_schedule));
        tvOpponent.setText(schedule.getOpponentTeam());
        tvWhen.setText(schedule.getMatchDate());
        tvWhere.setText(schedule.getMatchPlace());
        if (!schedule.getHomeScore().equals(String.valueOf(-1)) && !schedule.getOpponentScore().equals(String.valueOf(-1))) {
            tvHomeScore.setText(schedule.getHomeScore());
            tvOpponentScore.setText(schedule.getOpponentScore());
        }

    }

    @OnClick({R.id.record_schedule_btn_back, R.id.record_schedule_tv_confirm, R.id.record_schedule_tv_homescore, R.id.record_schedule_tv_opponentscore})
    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.record_schedule_btn_back:
                finish();
                break;
            case R.id.record_schedule_tv_confirm:
                confirmRecord();
                break;
            case R.id.record_schedule_tv_homescore:
                showScoreDialog();
                break;
            case R.id.record_schedule_tv_opponentscore:
                showScoreDialog();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        getPlayers();
    }

    private void initRecyclerView(List<Player> players) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        MemberAdapter memberAdapter = new MemberAdapter(this, teamCode, schedule.getSid());
        memberAdapter.setItemList(players);

        recyclerView.setAdapter(memberAdapter);
    }

    private void getPlayers() {
        players.clear();
        ProgressUtils.show(this, R.string.loading);

        //멤버 불러오기
        CloudService service = CloudService.retrofit.create(CloudService.class);
        Call<Team> call = service.callTeam(teamCode);
        call.enqueue(new Callback<Team>() {
            @Override
            public void onResponse(Call<Team> call, Response<Team> response) {
                if (!response.isSuccessful()) {
                    ProgressUtils.dismiss();
                    ToastUtils.showToast(getApplicationContext(), getString(R.string.loading_error));
                    return;
                }
                Team team = response.body();

                try {
                    if (team != null) {
                        JSONObject jsonObject = new JSONObject(team.getMembers().toString());
                        Iterator iterator = jsonObject.keys();
                        while (iterator.hasNext()) {
                            String key = (String) iterator.next();

                            JsonElement jsonElement = new JsonParser().parse(jsonObject.get(key).toString());
                            Player player = new Gson().fromJson(jsonElement, Player.class);
                            player.setPid(key);
                            players.add(player);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                initRecyclerView(players);
                ProgressUtils.dismiss();
            }

            @Override
            public void onFailure(Call<Team> call, Throwable t) {
                ProgressUtils.dismiss();

                //네트워크 에러
                Log.d(TAG, "onResponseFailed: " + call.request().url() + " " + t.getMessage());
            }
        });
    }

//    private void showHomeScoreDialog() {
//        final EditText edittext = new EditText(this);
//        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
//
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setTitle(getString(R.string.homescore));
//        alertDialogBuilder.setView(edittext);
//        alertDialogBuilder.setPositiveButton(getString(R.string.signup_confirm), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                tvHomeScore.setText(edittext.getText());
//            }
//        });
//        alertDialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//        alertDialogBuilder.show();
//    }
//
//    private void showOpponentScoreDialog() {
//        final EditText edittext = new EditText(this);
//        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
//
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setTitle(getString(R.string.opponentscore));
//        alertDialogBuilder.setView(edittext);
//        alertDialogBuilder.setPositiveButton(getString(R.string.signup_confirm), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                tvOpponentScore.setText(edittext.getText());
//            }
//        });
//        alertDialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//        alertDialogBuilder.show();
//    }

    private void showScoreDialog() {
        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_input_score, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);
        final AlertDialog ad = alertDialogBuilder.show();

        final EditText etHome = view.findViewById(R.id.dialog_input_score_et_home);
        final EditText etOpponent = view.findViewById(R.id.dialog_input_score_et_opponent);
        TextView tvConfirm = view.findViewById(R.id.dialog_input_score_tv_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String home = etHome.getText().toString();
                String opponent = etOpponent.getText().toString();
                if (home.matches("")) tvHomeScore.setText(getString(R.string.zero));
                else tvHomeScore.setText(etHome.getText().toString());
                if (opponent.matches("")) tvOpponentScore.setText(getString(R.string.zero));
                else tvOpponentScore.setText(opponent);
                ad.dismiss();
            }
        });
        Button btnCancel = view.findViewById(R.id.dialog_input_score_btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.dismiss();
            }
        });

    }

    private void confirmRecord() {
        String homeScore = tvHomeScore.getText().toString();
        String opponentScore = tvOpponentScore.getText().toString();
        if (!homeScore.matches("\\d+(?:\\.\\d+)?") || !opponentScore.matches("\\d+(?:\\.\\d+)?")) {
            ToastUtils.showToast(this, getString(R.string.not_number));
            return;
        }

        DatabaseReference reference = mDatabase.getReference("schedules").child(teamCode).child(schedule.getSid());
        reference.child("homeScore").setValue(homeScore);
        reference.child("opponentScore").setValue(opponentScore);
        for (int i = 0; i < players.size(); i++) {
            reference.child("records").child(players.get(i).getPid()).setValue(records.get(i));
        }

        finish();
    }
}
