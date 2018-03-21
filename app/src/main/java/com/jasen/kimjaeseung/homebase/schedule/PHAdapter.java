package com.jasen.kimjaeseung.homebase.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jasen.kimjaeseung.homebase.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimjaeseung on 2018. 3. 21..
 */

public class PHAdapter extends BaseAdapter {
    private List<PHListItem> phListItems = new ArrayList<>();

    public PHAdapter() {
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
        int position = i;
        Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listitem_record_member, viewGroup, false);
        }

        ImageView minus = (ImageView) view.findViewById(R.id.listitem_record_iv_minus);
        TextView menu = (TextView) view.findViewById(R.id.listitem_record_tv_menu);
        TextView num = (TextView) view.findViewById(R.id.listitem_record_tv_num);
        ImageView plus = (ImageView) view.findViewById(R.id.listitem_recod_iv_plus);

        PHListItem phListItem = phListItems.get(position);

        menu.setText(phListItem.getMenu());
        num.setText(String.valueOf(phListItem.getNum()));

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
