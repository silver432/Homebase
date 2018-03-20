package com.jasen.kimjaeseung.homebase.schedule;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.data.Player;
import com.jasen.kimjaeseung.homebase.data.Schedule;

import java.util.ArrayList;
import java.util.List;

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
        private TextView image;
        private TextView name;
        private TextView record;

        public MemberViewHolder(View itemView) {
            super(itemView);

            num = itemView.findViewById(R.id.listitem_member_tv_num);
            image = itemView.findViewById(R.id.listitem_member_civ);
            name = itemView.findViewById(R.id.listitem_member_tv_name);
            record = itemView.findViewById(R.id.listitem_member_tv_record);
        }

        public void bind(Player player){
            num.setText(String.valueOf(getAdapterPosition()+1));
            name.setText(player.getName());
        }
    }
}
