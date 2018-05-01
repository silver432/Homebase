package com.jasen.kimjaeseung.homebase.home;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.data.Schedule;
import com.jasen.kimjaeseung.homebase.data.Team;
import com.jasen.kimjaeseung.homebase.login.EnterTeamActivity;
import com.jasen.kimjaeseung.homebase.network.CloudService;
import com.jasen.kimjaeseung.homebase.util.DateUtils;
import com.jasen.kimjaeseung.homebase.util.ProgressUtils;
import com.jasen.kimjaeseung.homebase.util.SharedPrefUtils;
import com.jasen.kimjaeseung.homebase.util.ToastUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kimjaeseung on 2018. 3. 6..
 */

public class HomeFragment extends Fragment {
    private final static String TAG = HomeFragment.class.getSimpleName();
    private FirebaseStorage mStorage;
    private String teamCode;
    private List<Schedule> schedules = new ArrayList<>();
    private Fragment mFragnemt = this;

    @BindView(R.id.home_tv_name)
    TextView tvTeamName;
    @BindView(R.id.home_tv_description)
    TextView tvDescription;
    @BindView(R.id.home_civ_logo)
    CircleImageView civHomeLogo;
    @BindView(R.id.home_tv_date)
    TextView tvDate;
    @BindView(R.id.home_tv_day_of_week)
    TextView tvDayOfWeek;
    @BindView(R.id.home_view_bar)
    View bar;
    @BindView(R.id.home_tv_name2)
    TextView tvTeamName2;
    @BindView(R.id.home_tv_date2)
    TextView tvDate2;
    @BindView(R.id.home_tv_where2)
    TextView tvWhere2;
    @BindView(R.id.home_iv_next)
    ImageView ivNext;
    @BindView(R.id.home_fl_nextmatch)
    FrameLayout flNextMatch;

    public HomeFragment() {
    }

    public static Fragment getInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        init();

        return view;
    }

    private void init() {
        mStorage = FirebaseStorage.getInstance();

        teamCode = SharedPrefUtils.getTeamCode(this.getContext());

        Log.d(TAG, teamCode);

        //init view
        StorageReference storageReference = mStorage.getReference(teamCode + "/teamLogo");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getContext())
                        .load(uri)
                        .into(civHomeLogo);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

        //get team info
        ProgressUtils.show(getContext(), R.string.loading);

        CloudService service = CloudService.retrofit.create(CloudService.class);
        Call<Team> call = service.callTeam(teamCode);
        call.enqueue(new Callback<Team>() {
            @Override
            public void onResponse(Call<Team> call, Response<Team> response) {
                if (!response.isSuccessful()) {
                    //팀코드 존재하지 않음
                    ProgressUtils.dismiss();
                    ToastUtils.showToast(getActivity(), getString(R.string.code_not_exist));
                    return;
                }
                //팀코드 존재
                Team mTeam = response.body();

                //init view
                if (mTeam != null) {
                    tvTeamName.setText(mTeam.getName());
                    tvDescription.setText(mTeam.getDescription());
                }

                ProgressUtils.dismiss();
            }

            @Override
            public void onFailure(Call<Team> call, Throwable t) {
                ProgressUtils.dismiss();

                //네트워크 에러
                Log.d(TAG, "onResponseFailed: " + call.request().url() + " " + t.getMessage());
            }
        });

        Call<String> call1 = service.callSchedule(teamCode);
        call1.enqueue(new Callback<String>() {
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
                        schedule.setSid(key);
                        schedules.add(schedule);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Collections.sort(schedules, new Comparator<Schedule>() {
                    @Override
                    public int compare(Schedule s1, Schedule s2) {
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

                int lastGame = -1;
                for (int i = 0; i < schedules.size(); i++) {
                    Date date = null;
                    try {
                        date = sdf.parse(schedules.get(i).getMatchDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (date != null) {
                        if (i == 0 && date.compareTo(new Date()) < 0) {
                            // no next match
                            for (int index = 0; index < ((ViewGroup) flNextMatch).getChildCount(); ++index) {
                                View nextChild = ((ViewGroup) flNextMatch).getChildAt(index);
                                nextChild.setVisibility(View.INVISIBLE);
                            }
                            TextView tvNoNextMatch = new TextView(flNextMatch.getContext());
                            tvNoNextMatch.setTextSize(20);
                            String strNoNextMatch = mFragnemt.getString(R.string.no_next_match);

                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.gravity = Gravity.CENTER;

                            tvNoNextMatch.setText(strNoNextMatch);
                            flNextMatch.addView(tvNoNextMatch, layoutParams);
                            break;
                        }

                        if (date.compareTo(new Date()) > 0) {
                            lastGame = i;
                        }
                    }
                }

                if (lastGame != -1) {
                    Schedule nextSchedule = schedules.get(lastGame);
                    Calendar calendar = DateUtils.StringToCalendar(nextSchedule.getMatchDate());
                    tvDate.setText(String.valueOf(calendar.get(Calendar.DATE)));
                    tvDayOfWeek.setText(getDOW(calendar.get(Calendar.DAY_OF_WEEK)));
                    tvTeamName2.setText(nextSchedule.getOpponentTeam());
                    tvDate2.setText(calendarToString(calendar));
                    tvWhere2.setText(nextSchedule.getMatchPlace());
                }

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

    private String getDOW(int dow) {
        String strDow = null;
        switch (dow) {
            case Calendar.MONDAY:
                strDow = this.getString(R.string.monday);
                break;
            case Calendar.TUESDAY:
                strDow = this.getString(R.string.tuesday);
                break;
            case Calendar.WEDNESDAY:
                strDow = this.getString(R.string.wednesday);
                break;
            case Calendar.THURSDAY:
                strDow = this.getString(R.string.thursday);
                break;
            case Calendar.FRIDAY:
                strDow = this.getString(R.string.friday);
                break;
            case Calendar.SATURDAY:
                strDow = this.getString(R.string.saturday);
                break;
            case Calendar.SUNDAY:
                strDow = this.getString(R.string.sunday);
                break;
        }
        return strDow;
    }

    private String calendarToString(Calendar calendar) {
        return calendar.get(Calendar.YEAR) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.DATE) + " " + getDOW(calendar.get(Calendar.DAY_OF_WEEK));
    }

}
