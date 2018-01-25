package com.jasen.kimjaeseung.homebase.network;

import com.jasen.kimjaeseung.homebase.data.Player;
import com.jasen.kimjaeseung.homebase.data.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by kimjaeseung on 2018. 1. 23..
 */

public interface CloudService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://us-central1-base-45cd7.cloudfunctions.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("/getPlayer")
    Call<Player> callPlayer(@Query("uid") String uid);
    @GET("/getUser")
    Call<User> callUser(@Query("uid")String uid);
}
