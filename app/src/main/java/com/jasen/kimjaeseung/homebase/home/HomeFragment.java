package com.jasen.kimjaeseung.homebase.home;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jasen.kimjaeseung.homebase.R;
import com.jasen.kimjaeseung.homebase.login.EnterTeamActivity;
import com.jasen.kimjaeseung.homebase.util.SharedPrefUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kimjaeseung on 2018. 3. 6..
 */

public class HomeFragment extends Fragment {
    private final static String TAG = HomeFragment.class.getSimpleName();
    private FirebaseStorage mStorage;
    private String teamCode;

    @BindView(R.id.home_civ_logo)
    CircleImageView civHomeLogo;

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

        Log.d(TAG,teamCode);

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
    }
}
