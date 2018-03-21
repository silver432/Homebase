package com.jasen.kimjaeseung.homebase.schedule;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.data.Player;
import com.jasen.kimjaeseung.homebase.data.Schedule;
import com.jasen.kimjaeseung.homebase.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kimjaeseung on 2018. 3. 15..
 */

public class MemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = MemberAdapter.class.getSimpleName();

    private Context mContext;
    private List<Player> players = new ArrayList<>();

    public MemberAdapter(Context context){
        mContext = context;
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
        MemberViewHolder memberViewHolder = (MemberViewHolder)holder;
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

    public class MemberViewHolder extends RecyclerView.ViewHolder{
        private TextView num;
        private CircleImageView image;
        private TextView name;
        private TextView record;

        public MemberViewHolder(View itemView) {
            super(itemView);

            num = itemView.findViewById(R.id.listitem_member_tv_num);
            image = itemView.findViewById(R.id.listitem_member_civ);
            name = itemView.findViewById(R.id.listitem_member_tv_name);
            record = itemView.findViewById(R.id.listitem_member_tv_record);

            record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showRecordDialog();
                }
            });
        }

        public void bind(Player player){
            num.setText(String.valueOf(getAdapterPosition()+1));
            name.setText(player.getName());
        }

        private void showRecordDialog(){
            AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
            final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_record_member, null);

            TextView tvName = (TextView)view.findViewById(R.id.dialog_record_tv_name);
            final TextView tvHitter = (TextView)view.findViewById(R.id.dialog_record_tv_hitter);
            final TextView tvPitcher = (TextView)view.findViewById(R.id.dialog_record_tv_pitcher);


            tvName.setText(players.get(getAdapterPosition()).getName());

            makeHitterStrong(tvHitter,tvPitcher,view);
            tvHitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View clickView) {
                    makeHitterStrong(tvHitter,tvPitcher,view);
                }
            });
            tvPitcher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View clickView) {
                    makePitcherStrong(tvHitter,tvPitcher,view);
                }
            });

            adb.setView(view);
            adb.show();
        }

    }

    private void makeHitterStrong(TextView tvHitter,TextView tvPitcher,View view){
        String hitter = (mContext.getString(R.string.hitter));
        SpannableString content = new SpannableString(hitter);
        content.setSpan(new UnderlineSpan(), 0, hitter.length(), 0);
        content.setSpan(new StyleSpan(Typeface.BOLD), 0, hitter.length(), 0);
        tvHitter.setText(content);

        tvPitcher.setText(mContext.getString(R.string.pitcher));

        String[] inputHitter = mContext.getResources().getStringArray(R.array.hitter_input);
        makePHListView(view,inputHitter);

    }

    private void makePitcherStrong(TextView tvHitter,TextView tvPitcher,View view){
        String pitcher = (mContext.getString(R.string.pitcher));
        SpannableString content = new SpannableString(pitcher);
        content.setSpan(new UnderlineSpan(), 0,pitcher.length(), 0);
        content.setSpan(new StyleSpan(Typeface.BOLD), 0, pitcher.length(), 0);
        tvPitcher.setText(content);

        tvHitter.setText(mContext.getString(R.string.hitter));

        String[] inputPitcher = mContext.getResources().getStringArray(R.array.pitcher_input);
        makePHListView(view,inputPitcher);
    }

    private void makePHListView(View view,String[] strArray){
        ListView listView = (ListView)view.findViewById(R.id.dialog_record_lv);
        PHAdapter phAdapter = new PHAdapter();
        listView.setAdapter(phAdapter);

        phAdapter.itemListClear();

        for (int i=0;i<strArray.length;i++){
            phAdapter.addItem(strArray[i],i+1);
        }
    }
}
