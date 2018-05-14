package com.jasen.kimjaeseung.homebase.schedule;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.data.Player;
import com.jasen.kimjaeseung.homebase.data.Schedule;
import com.jasen.kimjaeseung.homebase.data.Team;
import com.jasen.kimjaeseung.homebase.login.LoginActivity;
import com.jasen.kimjaeseung.homebase.network.CloudService;
import com.jasen.kimjaeseung.homebase.util.DPUtills;
import com.jasen.kimjaeseung.homebase.util.DateUtils;
import com.jasen.kimjaeseung.homebase.util.ProgressUtils;
import com.jasen.kimjaeseung.homebase.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by kimjaeseung on 2018. 3. 10..
 */

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = ScheduleAdapter.class.getSimpleName();

    private Context mContext;
    private String teamCode;
    private List<Schedule> schedules = new ArrayList<>();
    private static final int SCHEDULE_NORMAL = 0;
    private static final int SCHEDULE_FIRST = 1;

    public ScheduleAdapter(Context context, String teamCode) {
        mContext = context;
        this.teamCode = teamCode;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case SCHEDULE_NORMAL:
                Context context = parent.getContext();

                int layoutIdForListItem = R.layout.listitem_schedule;
                LayoutInflater inflater = LayoutInflater.from(context);

                View view = inflater.inflate(layoutIdForListItem, parent, false);

                return new ScheduleViewHolder(view);
            case SCHEDULE_FIRST:
                Context context1 = parent.getContext();

                int layoutIdForListItem1 = R.layout.listitem_schedule_first;
                LayoutInflater inflater1 = LayoutInflater.from(context1);

                View view1 = inflater1.inflate(layoutIdForListItem1, parent, false);

                return new FirstScheduleViewHolder(view1);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case SCHEDULE_NORMAL:
                ScheduleViewHolder scheduleViewHolder = (ScheduleViewHolder) holder;
                scheduleViewHolder.bind(schedules.get(position));
                break;
            case SCHEDULE_FIRST:
                FirstScheduleViewHolder firstScheduleViewHolder = (FirstScheduleViewHolder) holder;
                firstScheduleViewHolder.bind(schedules.get(position));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Schedule schedule = schedules.get(position);

        int previousYear = -1;
        int currentYear = -1;
        int previousMonth = -1;
        int currentMonth = -1;
        if (position != 0) {
            previousMonth = DateUtils.StringToCalendar(schedules.get(position - 1).getMatchDate()).get(Calendar.MONTH);
            currentMonth = DateUtils.StringToCalendar(schedule.getMatchDate()).get(Calendar.MONTH);
            previousYear = DateUtils.StringToCalendar(schedules.get(position - 1).getMatchDate()).get(Calendar.YEAR);
            currentYear = DateUtils.StringToCalendar(schedule.getMatchDate()).get(Calendar.YEAR);
        }
        if (position == 0 || previousMonth != currentMonth || previousYear != currentYear)
            return SCHEDULE_FIRST;
        else return SCHEDULE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public void setItemList(List<Schedule> itemList) {
        schedules.clear();
        schedules.addAll(itemList);
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {
        private TextView dateFront;
        private TextView dayOfWeekFront;
        private TextView opponent;
        private TextView date;
        private TextView place;
        private TextView record;
        private LinearLayoutCompat linearLayoutCompat;

        public ScheduleViewHolder(final View itemView) {
            super(itemView);

            dateFront = (TextView) itemView.findViewById(R.id.listitem_tv_date_front);
            dayOfWeekFront = (TextView) itemView.findViewById(R.id.listitem_tv_day_of_week);
            opponent = (TextView) itemView.findViewById(R.id.listitem_tv_opponent);
            date = (TextView) itemView.findViewById(R.id.listitem_tv_date);
            place = (TextView) itemView.findViewById(R.id.listitem_tv_place);
            record = (TextView) itemView.findViewById(R.id.listitem_tv_record);
            linearLayoutCompat = itemView.findViewById(R.id.listitem_schedule_ll);

            //inner layout
            final View tmpView = LayoutInflater.from(mContext).inflate(R.layout.dialog_control_schedule, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            alertDialogBuilder.setView(tmpView);
            final AlertDialog alertDialog = alertDialogBuilder.create();

            LinearLayoutCompat llChange = tmpView.findViewById(R.id.dialog_control_schedule_ll_change);
            LinearLayoutCompat llDelete = tmpView.findViewById(R.id.dialog_control_schedule_ll_delete);
            llChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    goToRecordSchedule(schedules.get(position));
                    if (alertDialog.isShowing()) alertDialog.dismiss();
                }
            });
            llDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("schedules");
                    databaseReference.child(teamCode).child(schedules.get(getAdapterPosition()).getSid()).removeValue();
                    if (alertDialog.isShowing()) alertDialog.dismiss();
                }
            });

            linearLayoutCompat.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    alertDialog.show();
                    alertDialog.getWindow().setLayout(DPUtills.getPx(mContext, 250), WRAP_CONTENT);
                    return true;
                }
            });


            record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    goToRecordSchedule(schedules.get(position));
                }
            });
        }

        public void bind(Schedule schedule) {
            Calendar calendar = DateUtils.StringToCalendar(schedule.getMatchDate());
            int dow = calendar.get(Calendar.DAY_OF_WEEK);
            String strDow = null;
            switch (dow) {
                case Calendar.MONDAY:
                    strDow = mContext.getString(R.string.monday);
                    break;
                case Calendar.TUESDAY:
                    strDow = mContext.getString(R.string.tuesday);
                    break;
                case Calendar.WEDNESDAY:
                    strDow = mContext.getString(R.string.wednesday);
                    break;
                case Calendar.THURSDAY:
                    strDow = mContext.getString(R.string.thursday);
                    break;
                case Calendar.FRIDAY:
                    strDow = mContext.getString(R.string.friday);
                    break;
                case Calendar.SATURDAY:
                    strDow = mContext.getString(R.string.saturday);
                    break;
                case Calendar.SUNDAY:
                    strDow = mContext.getString(R.string.sunday);
                    break;
            }

            dateFront.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            dayOfWeekFront.setText(strDow);
            opponent.setText(schedule.getOpponentTeam());
            date.setText(schedule.getMatchDate());
            place.setText(schedule.getMatchPlace());

        }
    }

    public class FirstScheduleViewHolder extends RecyclerView.ViewHolder {
        private TextView month;
        private TextView year;
        private TextView dateFront;
        private TextView dayOfWeekFront;
        private TextView opponent;
        private TextView date;
        private TextView place;
        private TextView record;
        private LinearLayoutCompat linearLayoutCompat;

        public FirstScheduleViewHolder(View itemView) {
            super(itemView);

            month = (TextView) itemView.findViewById(R.id.listitem_first_tv_month);
            year = (TextView) itemView.findViewById(R.id.listitem_first_tv_year);
            dateFront = (TextView) itemView.findViewById(R.id.listitem_first_tv_date_front);
            dayOfWeekFront = (TextView) itemView.findViewById(R.id.listitem_first_tv_day_of_week);
            opponent = (TextView) itemView.findViewById(R.id.listitem_first_tv_opponent);
            date = (TextView) itemView.findViewById(R.id.listitem_first_tv_date);
            place = (TextView) itemView.findViewById(R.id.listitem_first_tv_place);
            record = (TextView) itemView.findViewById(R.id.listitem_first_tv_record);
            linearLayoutCompat = itemView.findViewById(R.id.listitem_schedule_first_ll);

            //inner layout
            final View tmpView = LayoutInflater.from(mContext).inflate(R.layout.dialog_control_schedule, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            alertDialogBuilder.setView(tmpView);
            final AlertDialog alertDialog = alertDialogBuilder.create();

            LinearLayoutCompat llChange = tmpView.findViewById(R.id.dialog_control_schedule_ll_change);
            LinearLayoutCompat llDelete = tmpView.findViewById(R.id.dialog_control_schedule_ll_delete);
            llChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    goToRecordSchedule(schedules.get(position));
                    if (alertDialog.isShowing()) alertDialog.dismiss();
                }
            });
            llDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("schedules");
                    databaseReference.child(teamCode).child(schedules.get(getAdapterPosition()).getSid()).removeValue();
                }
            });

            linearLayoutCompat.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    alertDialog.show();
                    alertDialog.getWindow().setLayout(DPUtills.getPx(mContext, 250), WRAP_CONTENT);
                    return true;
                }
            });

            record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    goToRecordSchedule(schedules.get(position));
                }
            });
        }

        public void bind(Schedule schedule) {
            Calendar calendar = DateUtils.StringToCalendar(schedule.getMatchDate());
            int dow = calendar.get(Calendar.DAY_OF_WEEK);
            String strDow = null;
            switch (dow) {
                case Calendar.MONDAY:
                    strDow = mContext.getString(R.string.monday);
                    break;
                case Calendar.TUESDAY:
                    strDow = mContext.getString(R.string.tuesday);
                    break;
                case Calendar.WEDNESDAY:
                    strDow = mContext.getString(R.string.wednesday);
                    break;
                case Calendar.THURSDAY:
                    strDow = mContext.getString(R.string.thursday);
                    break;
                case Calendar.FRIDAY:
                    strDow = mContext.getString(R.string.friday);
                    break;
                case Calendar.SATURDAY:
                    strDow = mContext.getString(R.string.saturday);
                    break;
                case Calendar.SUNDAY:
                    strDow = mContext.getString(R.string.sunday);
                    break;
            }

            month.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1));
            year.setText(String.valueOf(calendar.get(Calendar.YEAR)));
            dateFront.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            dayOfWeekFront.setText(strDow);
            opponent.setText(schedule.getOpponentTeam());
            date.setText(schedule.getMatchDate());
            place.setText(schedule.getMatchPlace());
            String strRecord = "";
            if (!schedule.getHomeScore().equals(String.valueOf(-1)) && !schedule.getOpponentScore().equals(String.valueOf(-1))) {
                strRecord = schedule.getHomeScore() + " : " + schedule.getOpponentScore();
                record.setTextSize(20);
                record.setText(strRecord);
            }
        }
    }

    private void goToRecordSchedule(Schedule schedule) {
        final Intent intent = new Intent(mContext, RecordScheduleActivity.class);
        intent.putExtra("schedule", schedule);
        intent.putExtra("teamCode", teamCode);
        mContext.startActivity(intent);
    }
}
