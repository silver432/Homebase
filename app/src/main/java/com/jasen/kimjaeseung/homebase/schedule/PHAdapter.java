package com.jasen.kimjaeseung.homebase.schedule;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jasen.kimjaeseung.homebase.R;

import java.util.ArrayList;
import java.util.List;

import static com.jasen.kimjaeseung.homebase.schedule.MemberAdapter.isHitterPressed;
import static com.jasen.kimjaeseung.homebase.schedule.MemberAdapter.records;

/**
 * Created by kimjaeseung on 2018. 3. 21..
 */

public class PHAdapter extends BaseAdapter {
    private static final String TAG = PHAdapter.class.getSimpleName();
    private List<PHListItem> phListItems = new ArrayList<>();
    private Context mContext;
    private int memberPosition; //member리스트의 position

    public PHAdapter(Context context,int memberPosition) {
        mContext = context;
        this.memberPosition = memberPosition;
    }

    @Override
    public int getCount() {
        return phListItems.size();
    }

    @Override
    public Object getItem(int i) {
        return phListItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int position = i;
        Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listitem_record_member, viewGroup, false);
        }

        ImageView minus = (ImageView) view.findViewById(R.id.listitem_record_iv_minus);
        TextView menu = (TextView) view.findViewById(R.id.listitem_record_tv_menu);
        final TextView num = (TextView) view.findViewById(R.id.listitem_record_tv_num);
        ImageView plus = (ImageView) view.findViewById(R.id.listitem_record_iv_plus);

        final PHListItem phListItem = phListItems.get(position);

        menu.setText(phListItem.getMenu());
        num.setText(String.valueOf(phListItem.getNum()));

        //minus click event
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = Integer.valueOf(num.getText().toString());

                if (number == 0) return;    //0일때 do not minus

                number -= 1;
                if (isHitterPressed) {  //타자가 선택되었을 때
                    if (position == 0) { //1루타
                        records.get(memberPosition).setSingleHit(number);
                    } else if (position == 1) {    //2루타
                        records.get(memberPosition).setDoubleHit(number);
                    } else if (position == 2) { //3루타
                        records.get(memberPosition).setTripleHit(number);
                    } else if (position == 3) { //홈런
                        records.get(memberPosition).setHomeRun(number);
                    } else if (position == 4) { //볼넷
                        records.get(memberPosition).setBaseOnBalls(number);
                    } else if (position == 5) { //사구
                        records.get(memberPosition).setHitByPitch(number);
                    } else if (position == 6) { //희생타
                        records.get(memberPosition).setSacrificeHit(number);
                    } else if (position == 7) { //도루
                        records.get(memberPosition).setStolenBase(number);
                    } else if (position == 8) { //삼진
                        records.get(memberPosition).setStrikeOut(number);
                    } else if (position == 9) { //땅볼
                        records.get(memberPosition).setGroundBall(number);
                    } else if (position == 10) {    //뜬공
                        records.get(memberPosition).setFlyBall(number);
                    } else if (position==11){   //득점
                        records.get(memberPosition).setRun(number);
                    } else if (position==12){   //타점
                        records.get(memberPosition).setRBI(number);
                    }
                }else { //투수가 선택되었을 때
                    if (position==0){   //승리
                        records.get(memberPosition).setWin(number);
                    }else if (position==1){ //패배
                        records.get(memberPosition).setLose(number);
                    }else if (position==2){ //홀드
                        records.get(memberPosition).setHold(number);
                    }else if (position==3){ //세이브
                        records.get(memberPosition).setSave(number);
                    }else if (position==4){ //이닝
                        records.get(memberPosition).setInning(number);
                    }else if (position==5){ //삼진
                        records.get(memberPosition).setStrikeOuts(number);
                    }else if (position==6){ //피안타
                        records.get(memberPosition).setHits(number);
                    }else if (position==7){ //홈런
                        records.get(memberPosition).setHomeRuns(number);
                    }else if (position==8){ //볼넷
                        records.get(memberPosition).setWalks(number);
                    }else if (position==9){ //사구
                        records.get(memberPosition).setHitBatters(number);
                    }else if (position==10){    //자책점
                        records.get(memberPosition).setER(number);
                    }
                }
                num.setText(String.valueOf(number));
            }
        });
        //plus click event
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = Integer.valueOf(num.getText().toString());
                number += 1;
                if (isHitterPressed) {  //타자가 선택되었을 때
                    if (position == 0) { //1루타
                        records.get(memberPosition).setSingleHit(number);
                    } else if (position == 1) {    //2루타
                        records.get(memberPosition).setDoubleHit(number);
                    } else if (position == 2) { //3루타
                        records.get(memberPosition).setTripleHit(number);
                    } else if (position == 3) { //홈런
                        records.get(memberPosition).setHomeRun(number);
                    } else if (position == 4) { //볼넷
                        records.get(memberPosition).setBaseOnBalls(number);
                    } else if (position == 5) { //사구
                        records.get(memberPosition).setHitByPitch(number);
                    } else if (position == 6) { //희생타
                        records.get(memberPosition).setSacrificeHit(number);
                    } else if (position == 7) { //도루
                        records.get(memberPosition).setStolenBase(number);
                    } else if (position == 8) { //삼진
                        records.get(memberPosition).setStrikeOut(number);
                    } else if (position == 9) { //땅볼
                        records.get(memberPosition).setGroundBall(number);
                    } else if (position == 10) {    //뜬공
                        records.get(memberPosition).setFlyBall(number);
                    } else if (position==11){   //득점
                        records.get(memberPosition).setRun(number);
                    } else if (position==12){   //타점
                        records.get(memberPosition).setRBI(number);
                    }
                }else { //투수가 선택되었을 때
                    if (position==0){   //승리
                        records.get(memberPosition).setWin(number);
                    }else if (position==1){ //패배
                        records.get(memberPosition).setLose(number);
                    }else if (position==2){ //홀드
                        records.get(memberPosition).setHold(number);
                    }else if (position==3){ //세이브
                        records.get(memberPosition).setSave(number);
                    }else if (position==4){ //이닝
                        records.get(memberPosition).setInning(number);
                    }else if (position==5){ //삼진
                        records.get(memberPosition).setStrikeOuts(number);
                    }else if (position==6){ //피안타
                        records.get(memberPosition).setHits(number);
                    }else if (position==7){ //홈런
                        records.get(memberPosition).setHomeRuns(number);
                    }else if (position==8){ //볼넷
                        records.get(memberPosition).setWalks(number);
                    }else if (position==9){ //사구
                        records.get(memberPosition).setHitBatters(number);
                    }else if (position==10){    //자책점
                        records.get(memberPosition).setER(number);
                    }
                }
                num.setText(String.valueOf(number));
            }
        });

        return view;
    }

    public void setItemList(List<PHListItem> p) {
        phListItems.clear();
        phListItems.addAll(p);
    }

    public void itemListClear() {
        phListItems.clear();
    }

    public void addItem(String menu, int num) {
        PHListItem phListItem = new PHListItem();
        phListItem.setMenu(menu);
        phListItem.setNum(num);
        phListItems.add(phListItem);
    }
}
