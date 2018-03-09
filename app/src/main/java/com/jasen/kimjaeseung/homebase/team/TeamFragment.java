package com.jasen.kimjaeseung.homebase.team;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jasen.kimjaeseung.homebase.R;

import butterknife.ButterKnife;

/**
 * Created by kimjaeseung on 2018. 3. 6..
 */

public class TeamFragment extends Fragment {
    private static final String TAG = TeamFragment.class.getSimpleName();

    public TeamFragment(){}

    public static Fragment getInstance(){
        return new TeamFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team, container, false);
        ButterKnife.bind(this,view);
        return view;
    }
}
