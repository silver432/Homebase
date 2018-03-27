package com.jasen.kimjaeseung.homebase.schedule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.data.Player;
import com.jasen.kimjaeseung.homebase.data.Schedule;
import com.jasen.kimjaeseung.homebase.data.Team;
import com.jasen.kimjaeseung.homebase.login.EnterTeamActivity;
import com.jasen.kimjaeseung.homebase.login.LoginActivity;
import com.jasen.kimjaeseung.homebase.main.MainActivity;
import com.jasen.kimjaeseung.homebase.network.CloudService;
import com.jasen.kimjaeseung.homebase.util.ProgressUtils;
import com.jasen.kimjaeseung.homebase.util.ToastUtils;
import com.squareup.picasso.Picasso;

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
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by kimjaeseung on 2018. 3. 6..
 */

public class ScheduleFragment extends Fragment {
    private final static String TAG = ScheduleFragment.class.getSimpleName();

    private String teamCode;
    private List<Schedule> schedules = new ArrayList<>();
    private FirebaseStorage mStorage;

    @BindView(R.id.schedule_civ_logo)
    CircleImageView civLogo;
    @BindView(R.id.schedule_cdl)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.schedule_sv)
    ScrollView scrollView;
    @BindView(R.id.schedule_fab)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.schedule_rv)
    RecyclerView recyclerView;

    public ScheduleFragment() {
    }

    public static Fragment getInstance() {
        return new ScheduleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        ButterKnife.bind(this, view);

        mStorage = FirebaseStorage.getInstance();

        //get teamcode in local
        SharedPreferences pref = getActivity().getSharedPreferences("teamPref", MODE_PRIVATE);
        teamCode = pref.getString("teamCode", "");

        //add scroll listener
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                View child = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
                int diff = (child.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

                if (diff == 0) floatingActionButton.hide();
                else floatingActionButton.show();
            }
        });

        //change logo
        StorageReference storageReference = mStorage.getReference(teamCode + "/teamLogo");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getContext())
                        .load(uri)
                        .into(civLogo);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        schedules.clear();

        ProgressUtils.show(getContext(), R.string.loading);

        //스케줄 동기화
        CloudService service = CloudService.retrofit.create(CloudService.class);
        Call<String> call = service.callSchedule(teamCode);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    //팀코드 없음
                    ProgressUtils.dismiss();
                    ToastUtils.showToast(getContext(), getString(R.string.code_not_exist));
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    Iterator iterator = jsonObject.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        JsonElement jsonElement = null;
                        jsonElement = new JsonParser().parse(jsonObject.get(key).toString());
                        Schedule schedule = new Gson().fromJson(jsonElement, Schedule.class);
                        schedules.add(schedule);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Collections.sort(schedules, new Comparator<Schedule>() {
                    @Override
                    public int compare(Schedule s1, Schedule s2) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Date s1Date = null;
                        Date s2Date = null;
                        try {
                            s1Date = sdf.parse(s1.getMatchDate());
                            s2Date = sdf.parse(s2.getMatchDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (s1Date == null || s2Date == null) return 0;
                        return s2Date.compareTo(s1Date);
                    }
                });

                initRecyclerView();

                ProgressUtils.dismiss();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.dismiss();

                //네트워크 에러
                Log.d(TAG, "onResponseFailed: " + call.request().url() + " " + t.getMessage());
            }
        });
    }

    @OnClick({R.id.schedule_fab})
    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.schedule_fab:
                goToAddSchedule();
                break;
        }
    }

    private void goToAddSchedule() {
        final Intent intent = new Intent(getContext(), AddScheudleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(this.getContext(), teamCode);
        scheduleAdapter.setItemList(schedules);

        recyclerView.setAdapter(scheduleAdapter);
    }

}
