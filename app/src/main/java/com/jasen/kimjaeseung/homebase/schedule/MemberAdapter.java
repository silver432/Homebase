package com.jasen.kimjaeseung.homebase.schedule;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.data.Player;
import com.jasen.kimjaeseung.homebase.data.Record;
import com.jasen.kimjaeseung.homebase.data.Schedule;
import com.jasen.kimjaeseung.homebase.data.Team;
import com.jasen.kimjaeseung.homebase.login.LoginActivity;
import com.jasen.kimjaeseung.homebase.network.CloudService;
import com.jasen.kimjaeseung.homebase.util.ProgressUtils;
import com.jasen.kimjaeseung.homebase.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kimjaeseung on 2018. 3. 15..
 */

public class MemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = MemberAdapter.class.getSimpleName();

    private Context mContext;
    private String teamCode;
    private String sid;
    private List<Player> players = new ArrayList<>();
    private List<Record> recordList = new ArrayList<>();
    public static boolean isHitterPressed = true;
    public static SparseArray<Record> records = new SparseArray<>();

    public MemberAdapter(Context context, String teamCode, String sid) {
        mContext = context;
        this.teamCode = teamCode;
        this.sid = sid;

        getRecords();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.listitem_schedule_member;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MemberViewHolder memberViewHolder = (MemberViewHolder) holder;
        memberViewHolder.bind(players.get(position));
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public void setItemList(List<Player> itemList) {
        players.clear();
        players.addAll(itemList);
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder {
        private TextView num;
        private TextView name;
        private TextView record;
        private LinearLayoutCompat ll;

        public MemberViewHolder(View itemView) {
            super(itemView);

            num = itemView.findViewById(R.id.listitem_member_tv_num);
            name = itemView.findViewById(R.id.listitem_member_tv_name);
            record = itemView.findViewById(R.id.listitem_member_tv_record);
            ll = itemView.findViewById(R.id.listitem_member_ll);

            record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    records.put(getAdapterPosition(), new Record());  //recreate record if click confirm
                    showRecordDialog(getAdapterPosition());
                }
            });
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMemberRecord(getAdapterPosition());
                }
            });

        }

        public void bind(Player player) {
            num.setText(String.valueOf(getAdapterPosition() + 1));
            name.setText(player.getName());
        }

        private void showRecordDialog(final int position) {   //기록용 dialog
            final AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
            final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_record_member, null);

            TextView tvName = (TextView) view.findViewById(R.id.dialog_record_tv_name);
            final TextView tvHitter = (TextView) view.findViewById(R.id.dialog_record_tv_hitter);
            final TextView tvPitcher = (TextView) view.findViewById(R.id.dialog_record_tv_pitcher);
            Button cancelButton = (Button) view.findViewById(R.id.dialog_record_btn_cancel);
            TextView confirmButton = (TextView) view.findViewById(R.id.dialog_record_tv_confirm);
            final ListView listView = (ListView) view.findViewById(R.id.dialog_record_lv);

            tvName.setText(players.get(getAdapterPosition()).getName());

            adb.setView(view);
            final AlertDialog ad = adb.show();

            isHitterPressed = true;
            makeHitterStrong(tvHitter, tvPitcher, view, position);
            tvHitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View clickView) {
                    isHitterPressed = true;
                    makeHitterStrong(tvHitter, tvPitcher, view, position);
                }
            });
            tvPitcher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View clickView) {
                    isHitterPressed = false;
                    makePitcherStrong(tvHitter, tvPitcher, view, position);
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {    //cancel record
                    records.put(getAdapterPosition(), new Record());
                    ad.dismiss();
                }
            });
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {    //confirm record
                    ad.dismiss();
                }
            });


        }

    }

    private void makeHitterStrong(TextView tvHitter, TextView tvPitcher, View view, int position) {
        String hitter = (mContext.getString(R.string.hitter));
        SpannableString content = new SpannableString(hitter);
        content.setSpan(new UnderlineSpan(), 0, hitter.length(), 0);
        content.setSpan(new StyleSpan(Typeface.BOLD), 0, hitter.length(), 0);
        tvHitter.setText(content);

        tvPitcher.setText(mContext.getString(R.string.pitcher));

        String[] inputHitter = mContext.getResources().getStringArray(R.array.hitter_input);
        makePHListView(view, inputHitter, position);
    }

    private void makePitcherStrong(TextView tvHitter, TextView tvPitcher, View view, int position) {
        String pitcher = (mContext.getString(R.string.pitcher));
        SpannableString content = new SpannableString(pitcher);
        content.setSpan(new UnderlineSpan(), 0, pitcher.length(), 0);
        content.setSpan(new StyleSpan(Typeface.BOLD), 0, pitcher.length(), 0);
        tvPitcher.setText(content);

        tvHitter.setText(mContext.getString(R.string.hitter));

        String[] inputPitcher = mContext.getResources().getStringArray(R.array.pitcher_input);
        makePHListView(view, inputPitcher, position);
    }

    private void makeHitterStrong2(TextView tvHitter, TextView tvPitcher, View view, int position) {
        String hitter = (mContext.getString(R.string.hitter));
        SpannableString content = new SpannableString(hitter);
        content.setSpan(new UnderlineSpan(), 0, hitter.length(), 0);
        content.setSpan(new StyleSpan(Typeface.BOLD), 0, hitter.length(), 0);
        tvHitter.setText(content);

        tvPitcher.setText(mContext.getString(R.string.pitcher));

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View child = inflater.inflate(R.layout.view_hitter, null);

        TextView tvHitterSingle= child.findViewById(R.id.view_hitter_tv_single);
        TextView tvHitterDouble =child.findViewById(R.id.view_hitter_tv_two_base);
        TextView tvHitterTriple=child.findViewById(R.id.view_hitter_tv_three_base);
        TextView tvHitterHomeRun=child.findViewById(R.id.view_hitter_tv_homerun);
        TextView tvHitterWalks=child.findViewById(R.id.view_hitter_tv_walks);
        TextView tvHitterSacrifice=child.findViewById(R.id.view_hitter_tv_sacrifice_hit);
        TextView tvHitterStrikeOuts=child.findViewById(R.id.view_hitter_tv_strikeoouts);
        TextView tvHitterGrounder=child.findViewById(R.id.view_hitter_tv_grounder);
        TextView tvHitterFlyBall=child.findViewById(R.id.view_hitter_tv_fly_ball);
        TextView tvHitterStolen=child.findViewById(R.id.view_hitter_tv_stolen);
        TextView tvHitterHit=child.findViewById(R.id.view_hitter_tv_hit);
        TextView tvHitterGoal=child.findViewById(R.id.view_hitter_tv_goal);
        TextView tvHitterRBI=child.findViewById(R.id.view_hitter_tv_rbi);

        //해당 선수에관한 record 가져오기
        Record record = getRecord(position);
        if (record !=null){
            //hitter 정보표시
            tvHitterSingle.setText(String.valueOf(record.getSingleHit()));
            tvHitterDouble.setText(String.valueOf(record.getDoubleHit()));
            tvHitterTriple.setText(String.valueOf(record.getTripleHit()));
            tvHitterHomeRun.setText(String.valueOf(record.getHomeRun()));
            tvHitterWalks.setText(String.valueOf(record.getBaseOnBalls()));
            tvHitterSacrifice.setText(String.valueOf(record.getSacrificeHit()));
            tvHitterStrikeOuts.setText(String.valueOf(record.getStrikeOut()));
            tvHitterGrounder.setText(String.valueOf(record.getGroundBall()));
            tvHitterFlyBall.setText(String.valueOf(record.getFlyBall()));
            tvHitterStolen.setText(String.valueOf(record.getStolenBase()));
            tvHitterHit.setText(String.valueOf(record.getHitByPitch()));
            tvHitterGoal.setText(String.valueOf(record.getRun()));
            tvHitterRBI.setText(String.valueOf(record.getRBI()));
        }

        LinearLayout container = view.findViewById(R.id.dialog_bottom_member_ll_container);
        container.removeAllViews();
        container.addView(child);
    }

    private void makePitcherStrong2(TextView tvHitter, TextView tvPitcher, View view, int position) {
        String pitcher = (mContext.getString(R.string.pitcher));
        SpannableString content = new SpannableString(pitcher);
        content.setSpan(new UnderlineSpan(), 0, pitcher.length(), 0);
        content.setSpan(new StyleSpan(Typeface.BOLD), 0, pitcher.length(), 0);
        tvPitcher.setText(content);

        tvHitter.setText(mContext.getString(R.string.hitter));

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View child = inflater.inflate(R.layout.view_pitcher, null);

        TextView victory=child.findViewById(R.id.view_pitcher_tv_victory);
        TextView defeat=child.findViewById(R.id.view_pitcher_tv_defeat);
        TextView hold=child.findViewById(R.id.view_pitcher_tv_hold);
        TextView homerun=child.findViewById(R.id.view_pitcher_tv_homerun);
        TextView walks=child.findViewById(R.id.view_pitcher_tv_walks);
        TextView hitBatters=child.findViewById(R.id.view_pitcher_tv_hit);
        TextView hits = child.findViewById(R.id.view_pitcher_tv_hits);
        TextView save = child.findViewById(R.id.view_pitcher_tv_save);
        TextView inning=child.findViewById(R.id.view_pitcher_tv_inning);
        TextView strikeOut=child.findViewById(R.id.view_pitcher_tv_strikeouts);
        TextView ER=child.findViewById(R.id.view_pitcher_er);

        Record record = getRecord(position);
        if (record!=null){
            victory.setText(String.valueOf(record.getWin()));
            defeat.setText(String.valueOf(record.getLose()));
            hold.setText(String.valueOf(record.getHold()));
            homerun.setText(String.valueOf(record.getHomeRuns()));
            walks.setText(String.valueOf(record.getWalks()));
            hitBatters.setText(String.valueOf(record.getHitBatters()));
            hits.setText(String.valueOf(record.getHits()));
            save.setText(String.valueOf(record.getSave()));
            inning.setText(String.valueOf(record.getInning()));
            strikeOut.setText(String.valueOf(record.getStrikeOuts()));
            ER.setText(String.valueOf(record.getER()));
        }

        LinearLayout container = view.findViewById(R.id.dialog_bottom_member_ll_container);
        container.removeAllViews();
        container.addView(child);
    }

    private void makePHListView(View view, String[] strArray, int position) {
        ListView listView = (ListView) view.findViewById(R.id.dialog_record_lv);
        PHAdapter phAdapter = new PHAdapter(mContext, position);
        listView.setAdapter(phAdapter);

        phAdapter.itemListClear();

        for (int i = 0; i < strArray.length; i++) {
            int currentNum = 0;
            if (isHitterPressed) {  //타자가 선택되었을 때
                if (i == 0) { //1루타
                    currentNum = records.get(position).getSingleHit();
                } else if (i == 1) {    //2루타
                    currentNum = records.get(position).getDoubleHit();
                } else if (i == 2) { //3루타
                    currentNum = records.get(position).getTripleHit();
                } else if (i == 3) { //홈런
                    currentNum = records.get(position).getHomeRun();
                } else if (i == 4) { //볼넷
                    currentNum = records.get(position).getBaseOnBalls();
                } else if (i == 5) { //사구
                    currentNum = records.get(position).getHitByPitch();
                } else if (i == 6) { //희생타
                    currentNum = records.get(position).getSacrificeHit();
                } else if (i == 7) { //도루
                    currentNum = records.get(position).getStolenBase();
                } else if (i == 8) { //삼진
                    currentNum = records.get(position).getStrikeOut();
                } else if (i == 9) { //땅볼
                    currentNum = records.get(position).getGroundBall();
                    ;
                } else if (i == 10) {    //뜬공
                    currentNum = records.get(position).getFlyBall();
                } else if (i == 11) {   //득점
                    currentNum = records.get(position).getRun();
                } else if (i == 12) {   //타점
                    currentNum = records.get(position).getRBI();
                }
            } else { //투수가 선택되었을 때
                if (i == 0) {   //승리
                    currentNum = records.get(position).getWin();
                } else if (i == 1) { //패배
                    currentNum = records.get(position).getLose();
                } else if (i == 2) { //홀드
                    currentNum = records.get(position).getHold();
                } else if (i == 3) { //세이브
                    currentNum = records.get(position).getSave();
                } else if (i == 4) { //이닝
                    currentNum = records.get(position).getInning();
                } else if (i == 5) { //삼진
                    currentNum = records.get(position).getStrikeOuts();
                } else if (i == 6) { //피안타
                    currentNum = records.get(position).getHits();
                } else if (i == 7) { //홈런
                    currentNum = records.get(position).getHomeRuns();
                } else if (i == 8) { //볼넷
                    currentNum = records.get(position).getWalks();
                } else if (i == 9) { //사구
                    currentNum = records.get(position).getHitBatters();
                } else if (i == 10) {    //자책점
                    currentNum = records.get(position).getER();
                }
            }
            phAdapter.addItem(strArray[i], currentNum);
        }
    }

    private void showMemberRecord(final int position) {   //보여주기용 dialog
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_member_bottom, null);

        TextView tvName = view.findViewById(R.id.dialog_bottom_member_tv_name);
        final TextView tvHitter = view.findViewById(R.id.dialog_bottom_member_tv_hitter);
        final TextView tvPitcher = view.findViewById(R.id.dialog_bottom_member_tv_pitcher);
        Button cancelButton = view.findViewById(R.id.dialog_bottom_member_btn_cancel);

        tvName.setText(players.get(position).getName());

        isHitterPressed = true;
        makeHitterStrong2(tvHitter, tvPitcher, view, position);
        tvHitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickView) {
                isHitterPressed = true;
                makeHitterStrong2(tvHitter, tvPitcher, view, position);
            }
        });
        tvPitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickView) {
                isHitterPressed = false;
                makePitcherStrong2(tvHitter, tvPitcher, view, position);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void getRecords() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("schedules").child(teamCode).child(sid).child("records");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Record record = dataSnapshot.getValue(Record.class);
                record.setUid(dataSnapshot.getKey());
                recordList.add(record);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Record getRecord(int position) {
        for (int i = 0; i < recordList.size(); i++) {
            if (recordList.get(i).getUid().equals(players.get(position).getPid())) {
                return recordList.get(i);
            }
        }
        return null;
    }
}
