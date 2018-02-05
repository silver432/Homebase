package com.jasen.kimjaeseung.homebase.network;

import com.jasen.kimjaeseung.homebase.data.Player;
import com.jasen.kimjaeseung.homebase.data.PostEmailByName;
import com.jasen.kimjaeseung.homebase.data.PostRequestEmail;
import com.jasen.kimjaeseung.homebase.data.Team;
import com.jasen.kimjaeseung.homebase.data.User;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by kimjaeseung on 2018. 1. 23..
 */

public interface CloudService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://us-central1-base-45cd7.cloudfunctions.net/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("getPlayer")
    Call<Player> callPlayer(@Query("uid") String uid);

    @GET("getUser")
    Call<User> callUser(@Query("uid") String uid);

    @Headers("Content-Type: application/json")
    @POST("findEmail")
    Call<String> findEmail(@Body PostRequestEmail postRequestEmail);

    @Headers("Content-Type: application/json")
    @POST("checkEmailByName")
    Call<String> checkEmailByName(@Body PostEmailByName postEmailByName);

    @GET("getTeam")
    Call<Team> callTeam(@Query("teamCode")String teamCode);
}
